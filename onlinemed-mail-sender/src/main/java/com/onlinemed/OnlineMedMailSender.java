package com.onlinemed;

import com.onlinemed.properties.HibernateProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(HibernateProperties.class)
public class OnlineMedMailSender {

    public static void main(String[] args) {
        SpringApplication.run(OnlineMedMailSender.class, args);
    }

}
