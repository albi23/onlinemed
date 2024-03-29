package com.onlinemed.features.mail.cfg;

import com.onlinemed.features.mail.dto.MailPayload;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.UUID;

import static com.onlinemed.features.mail.KafkaTopicsDefs.GENERAL_DEAD;

@Configuration
class KafkaErrorhandler {


    private final KafkaProperties kafkaProperties;

    KafkaErrorhandler(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }


    @Bean
    public ConsumerFactory<Object, Object> consumerFactory() {
        var properties = kafkaProperties.buildConsumerProperties((SslBundles)null);
        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean(name = "kafkaMailListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<Object, Object>
    kafkaMailListenerContainerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer, KafkaTemplate<UUID, Object> kafkaTemplate) {
        var factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, consumerFactory());
        factory.setCommonErrorHandler(exceptionHandler(kafkaTemplate));
        return factory;
    }

    CommonErrorHandler exceptionHandler(KafkaTemplate<UUID, Object> kafkaTemplate) {
        var handler = new DefaultErrorHandler(getDeadLetterPublishingRecoverer(kafkaTemplate),
                new FixedBackOff(500L, 3L));
        handler.addNotRetryableExceptions(IllegalArgumentException.class);
        handler.addNotRetryableExceptions(SerializationException.class);
        return handler;
    }

    static DeadLetterPublishingRecoverer getDeadLetterPublishingRecoverer(KafkaTemplate<UUID, Object> kafkaTemplate) {
        return new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> {
                    switch (record.value()) {
                        case MailPayload _ -> new TopicPartition(STR."\{record.topic()}-dead", record.partition());
                        default -> throw new IllegalStateException(STR."Unexpected value: \{record.value()}");
                    }
                    return new TopicPartition(GENERAL_DEAD, record.partition());
                }
        );
    }
}
