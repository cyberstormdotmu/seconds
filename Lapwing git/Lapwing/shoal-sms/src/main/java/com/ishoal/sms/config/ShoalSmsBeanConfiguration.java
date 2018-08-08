package com.ishoal.sms.config;

import com.ishoal.sms.SmsService;
import com.ishoal.sms.dummy.LoggingSmsService;
import com.ishoal.sms.textmagic.TextMagicSmsService;
import com.textmagic.sdk.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShoalSmsBeanConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ShoalSmsBeanConfiguration.class);

    @Value("${shoal.sms.enabled:true}")
    private boolean smsEnabled;

    @Value("${shoal.sms.textMagicUserName:}")
    private String textMagicUserName;

    @Value("${shoal.sms.textMagicApiKey:}")
    private String textMagicApiKey;

    @Bean
    public RestClient textMagicRestClient() {
        if(smsEnabled) {
            return new RestClient(textMagicUserName, textMagicApiKey);
        }
        return null;
    }

    @Bean
    public SmsService smsService() {
        if(smsEnabled) {
            logger.info("Configuring live SMS service");
            return new TextMagicSmsService(textMagicRestClient());
        }
        logger.info("Configuring logging SMS service");
        return new LoggingSmsService();
    }
}
