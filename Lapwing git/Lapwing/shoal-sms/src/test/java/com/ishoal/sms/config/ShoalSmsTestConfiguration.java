package com.ishoal.sms.config;

import com.ishoal.sms.SmsService;
import com.ishoal.sms.fake.FakeSmsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoalSmsTestConfiguration {

    @Bean
    public SmsService smsService() {
        return new FakeSmsService();
    }
}
