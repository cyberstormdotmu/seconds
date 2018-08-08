package com.ishoal.email.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ShoalEmailBeanConfiguration.class})
public class ShoalEmailConfiguration {

}
