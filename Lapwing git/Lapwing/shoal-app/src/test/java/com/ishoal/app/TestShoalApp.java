package com.ishoal.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.ishoal.core.config.ShoalCoreConfiguration;
import com.ishoal.email.config.ShoalEmailConfiguration;
import com.ishoal.payment.buyer.config.ShoalPaymentTestConfiguration;
import com.ishoal.ws.config.ShoalWsConfiguration;

@SpringBootApplication
@Configuration
@Import({ShoalWsConfiguration.class, ShoalCoreConfiguration.class, ShoalEmailConfiguration.class, ShoalPaymentTestConfiguration.class})
public class TestShoalApp {
    public static void main(String[] args) {
        final SpringApplication application = new SpringApplication(ShoalApp.class);
        application.setWebEnvironment(true);
        application.run(args);
    }
}
