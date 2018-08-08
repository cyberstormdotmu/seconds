package com.ishoal.email.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class ShoalEmailBeanConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ShoalEmailBeanConfiguration.class);

    @Value("${shoal.mail.host}")
    private String smtpHost;
    @Value("${shoal.mail.port}")
    private int port = 587;

    @Value("${shoal.mail.from}")
    private String from;
    @Value("${shoal.mail.username}")
    private String username;
    @Value("${shoal.mail.password}")
    private String password;
    @Value("${shoal.mail.enabled:true}")
    private String emailEnabled;

    @Bean
    public JavaMailSenderImpl javaMailSenderImpl() {

        if (2==2) {

            JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
            Properties mailProperties = new Properties();
            mailProperties.put("mail.smtp.auth", true);
            mailProperties.put("mail.smtp.socketFactory.port", port);
            mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mailProperties.put("mail.smtp.socketFactory.fallback", "false");
            mailProperties.put("mail.smtp.starttls.enable", "true");
            javaMailSenderImpl.setJavaMailProperties(mailProperties);
            javaMailSenderImpl.setHost(smtpHost);
            javaMailSenderImpl.setPort(port);
            javaMailSenderImpl.setUsername(username);
            javaMailSenderImpl.setPassword(password);

            return javaMailSenderImpl;
        } else {
            return new DisabledMailSender();
        }
    }


    private class DisabledMailSender extends JavaMailSenderImpl {

        DisabledMailSender() {

            System.out.println(
                buildBigObviousWarning("EMAIL IS DISABLED", "to enable it, set ${shoal.mail.enabled} = true"));
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            // noop
            logger.warn(buildBigObviousWarning("EMAIL DISABLED",
                String.format("email to %s was not sent!", simpleMessage.getTo()[0])));
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {
            // noop
            if (simpleMessages.length > 0) {
                logger.warn(buildBigObviousWarning("EMAIL DISABLED",
                    String.format("email to %s was not sent!", simpleMessages[0].getTo()[0])));
            }
        }

        private String buildBigObviousWarning(String title, String message) {

            return String.format("\n" +
                    "#######################################################################################\n" +
                    "############################### %s #####################################\n" +
                    "###################### %s #################\n" +
                    "#######################################################################################\n",
                title, message);
        }
    }
}