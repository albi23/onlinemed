package com.onlinemed.features.mail.cfg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Slf4j
@Configuration
class MailSenderConfig {

    @Bean
    JavaMailSenderImpl mailSender() {
        var mailSender = new JavaMailSenderImpl();
        var fileTypeMap = new ConfigurableMimeFileTypeMap();
        fileTypeMap.afterPropertiesSet();
        mailSender.setDefaultFileTypeMap(fileTypeMap);

        final String username = System.getenv("SPRING_MAIL_USERNAME");
        final String password = System.getenv("SPRING_MAIL_PASSWORD");
        final String herokuStatus = System.getenv("HEROKU_STATUS");
        if (Boolean.TRUE.toString().toUpperCase().equals(herokuStatus)) {
            final Properties props = mailSender.getJavaMailProperties();
            props.setProperty("mail.imap.ssl.enable", "true");
            props.setProperty("mail.imap.sasl.enable", "true");
            props.put("mail.imap.sasl.mechanisms", "XOAUTH2");
            props.put("mail.imap.auth.login.disable", "true");
            props.put("mail.imap.auth.plain.disable", "true");
            mailSender.setHost("imap.gmail.com");
            /**TODO: AOUTH gmail tooken implementation required for heroku
                mailSender.setPassword();
                mailSender.setUsername(username);
            */
        }
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            mailSender.setPassword(password);
            mailSender.setUsername(username);
        } else {
             var str = "\n" + "_".repeat(113);
            log.error(str + "\nMail configuration is not set. Please set up environment variable  SPRING_MAIL_USERNAME and SPRING_MAIL_PASSWORD" + str);
        }

        return mailSender;
    }
}
