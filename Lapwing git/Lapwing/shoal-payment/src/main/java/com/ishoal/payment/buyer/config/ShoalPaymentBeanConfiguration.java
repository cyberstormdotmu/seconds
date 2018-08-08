package com.ishoal.payment.buyer.config;

import com.ishoal.payment.buyer.BuyerPaymentService;
import com.ishoal.payment.buyer.stripe.StripeBuyerPaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("stripePayment")
public class ShoalPaymentBeanConfiguration {

    @Value("${shoal.stripeApiKey}")
    private String stripeApiKey;

    @Bean
    public BuyerPaymentService buyerPaymentService() {
        return new StripeBuyerPaymentService(stripeApiKey);
    }
}
