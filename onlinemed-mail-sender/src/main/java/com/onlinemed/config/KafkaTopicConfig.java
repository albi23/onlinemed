package com.onlinemed.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.onlinemed.config.KafkaTopicsDefs.MAIL_RECEIVE;

@Slf4j
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(MAIL_RECEIVE)
                .partitions(3)
                .replicas(1)
                .build();
    }


}
