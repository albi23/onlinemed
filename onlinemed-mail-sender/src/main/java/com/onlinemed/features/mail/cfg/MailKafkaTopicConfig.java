package com.onlinemed.features.mail.cfg;

import com.onlinemed.features.mail.KafkaTopicsDefs;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Slf4j
@Configuration
class MailKafkaTopicConfig {

    @Bean
    public NewTopic mailReceiveTopic() {
        return TopicBuilder.name(KafkaTopicsDefs.MAIL_RECEIVE)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic mailReceiveTopicDead() {
        return TopicBuilder.name(KafkaTopicsDefs.MAIL_RECEIVE_DED)
                .partitions(2)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic exceptionDeadEnd() {
        return TopicBuilder.name(KafkaTopicsDefs.GENERAL_DEAD)
                .partitions(2)
                .replicas(1)
                .build();
    }


}
