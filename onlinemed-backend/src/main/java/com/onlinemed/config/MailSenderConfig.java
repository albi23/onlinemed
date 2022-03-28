package com.onlinemed.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class MailSenderConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSenderConfig.class);

    @Bean
    void getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        ConfigurableMimeFileTypeMap fileTypeMap = new ConfigurableMimeFileTypeMap();
        fileTypeMap.afterPropertiesSet();
        mailSender.setDefaultFileTypeMap(fileTypeMap);

        final String username = System.getenv("SPRING_MAIL_USERNAME");
        final String password = System.getenv("SPRING_MAIL_PASSWORD");
        final String herokuStatus = System.getenv("HEROKU_STATUS");
        if ("TRUE".equals(herokuStatus)) {
            final Properties props = mailSender.getJavaMailProperties();
            props.setProperty("mail.imap.ssl.enable", "true");
            props.setProperty("mail.imap.sasl.enable", "true");
            props.put("mail.imap.sasl.mechanisms", "XOAUTH2");
            props.put("mail.imap.auth.login.disable", "true");
            props.put("mail.imap.auth.plain.disable", "true");
            mailSender.setHost("imap.gmail.com");
            /*TODO: AOUTH gmail tooken implementation required for heroku
            mailSender.setPassword();
            mailSender.setUsername(username);
            */
        }
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            mailSender.setPassword(password);
            mailSender.setUsername(username);
        } else {
            final String str = "\n" + IntStream.range(1, 113).mapToObj(i -> "_").collect(Collectors.joining());
            LOGGER.error(str + "\nMail configuration is not set. Please set up environment variable  SPRING_MAIL_USERNAME and SPRING_MAIL_PASSWORD" + str);
        }
    }
}
