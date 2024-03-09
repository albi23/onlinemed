package com.onlinemed.config;

import com.onlinemed.model.dto.NotificationDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.onlinemed.config.KafkaTopicsDefs.GENERAL_DEAD;

@Configuration
public class KafkaConsumerConfig {

    private record ConsumerConfigProps(String CLIENT_ID, String GROUP_ID_CONFIG){}

    private static final ConsumerConfigProps props = new ConsumerConfigProps(
            "onlinemed-service-receiver",
            "onlinemed-service-receive-group"
    );

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;


    @Bean
    public ConsumerFactory<Object, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean(name = "kafkaNotificationListener")
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
                    if (Objects.requireNonNull(record.value()) instanceof NotificationDto) {
                        new TopicPartition(record.topic()+"-dead", record.partition());
                    } else {
                        throw new IllegalStateException("Unexpected value: " + record.value());
                    }
                    return new TopicPartition(GENERAL_DEAD, record.partition());
                }
        );
    }
    private Map<String, Object> consumerConfigs() {
        return Map.ofEntries(
                Map.entry(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers),
                Map.entry(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class),
                Map.entry(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class),
                Map.entry(ConsumerConfig.CLIENT_ID_CONFIG, props.CLIENT_ID),
                Map.entry(ConsumerConfig.GROUP_ID_CONFIG, props.GROUP_ID_CONFIG)
        );
    }
}
