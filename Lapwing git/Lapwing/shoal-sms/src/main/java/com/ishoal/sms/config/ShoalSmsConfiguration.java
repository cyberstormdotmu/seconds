package com.ishoal.sms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ShoalSmsBeanConfiguration.class})
public class ShoalSmsConfiguration {
}
