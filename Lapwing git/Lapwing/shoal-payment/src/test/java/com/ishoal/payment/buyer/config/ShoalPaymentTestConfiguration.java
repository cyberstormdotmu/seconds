package com.ishoal.payment.buyer.config;

import com.ishoal.payment.buyer.BuyerPaymentService;
import com.ishoal.payment.buyer.FakeBuyerPaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoalPaymentTestConfiguration {

    @Bean
    public BuyerPaymentService buyerPaymentService() {
        return new FakeBuyerPaymentService();
    }
}
