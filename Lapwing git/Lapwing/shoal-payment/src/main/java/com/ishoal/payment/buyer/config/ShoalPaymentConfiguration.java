package com.ishoal.payment.buyer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ShoalPaymentBeanConfiguration.class})
public class ShoalPaymentConfiguration {
}
