package com.onlinemed.features.mail.cfg;

import jakarta.mail.Address;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.function.ThrowingSupplier;

import java.util.Arrays;
import java.util.Properties;

import static java.util.Spliterator.ORDERED;
import static java.util.Spliterators.spliteratorUnknownSize;
import static java.util.stream.Collectors.joining;
import static java.util.stream.StreamSupport.stream;

@Slf4j
@Configuration
class MailSenderConfig {

    @Bean
    @Profile("!dev")
    JavaMailSender emailSender() {
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
            var str = STR."\n\{"_".repeat(113)}";
            log.error(STR."\{str}\nMail configuration is not set. Please set up environment variable  SPRING_MAIL_USERNAME and SPRING_MAIL_PASSWORD\{str}");
        }

        return mailSender;
    }

    @Bean
    @Profile("dev")
    JavaMailSender mailSenderDev() {
        return new DummyMailSender();
    }


    static class DummyMailSender extends JavaMailSenderImpl {
        private final Logger logger = LoggerFactory.getLogger(DummyMailSender.class);

        public DummyMailSender() {
            super();
            logger.info("Using dev dummy mail service implementation");
        }

        @Override
        public void send(MimeMessage mimeMessage) throws MailException {
            ThrowingSupplier<String> stringThrowingSupplier = () ->
                    stream(spliteratorUnknownSize(mimeMessage.getAllHeaders().asIterator(), ORDERED), false)
                            .map(Object::toString).collect(joining());
            logger.info(STR."""
                    Should send mail:
                    From: \{Arrays.toString(getOrDefault(mimeMessage::getFrom, new Address[0]))}
                    Headers: \{getOrDefault(stringThrowingSupplier, "")}
                    """);

        }

        @Override
        public void testConnection() {
            logger.info("Dummy email service always works!");
        }

        private <R> R getOrDefault(ThrowingSupplier<R> throwingFunction, R defaultVal) {
            try {
                return throwingFunction.get();
            } catch (Exception ex) {
                logger.info("Exception occurred. Returning default val ", ex);
                return defaultVal;
            }
        }


    }
}
