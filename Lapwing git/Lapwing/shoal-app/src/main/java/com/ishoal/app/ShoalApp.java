package com.ishoal.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.ishoal.core.config.ShoalCoreConfiguration;
import com.ishoal.email.config.ShoalEmailConfiguration;
import com.ishoal.payment.buyer.config.ShoalPaymentConfiguration;
import com.ishoal.sms.config.ShoalSmsConfiguration;
import com.ishoal.ws.config.ShoalWsConfiguration;

@SpringBootApplication
@Configuration
@EnableScheduling
@Import({ShoalWsConfiguration.class,ShoalCoreConfiguration.class,
	ShoalEmailConfiguration.class, ShoalPaymentConfiguration.class,
	ShoalSmsConfiguration.class})
public class ShoalApp {

	public static void main(String[] args) {
		final SpringApplication application = new SpringApplication(ShoalApp.class);
		application.setWebEnvironment(true);
		application.run(args);
	}
}
