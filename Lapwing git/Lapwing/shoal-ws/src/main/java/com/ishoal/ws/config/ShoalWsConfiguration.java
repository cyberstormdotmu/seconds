package com.ishoal.ws.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({ShoalWsBeanConfiguration.class, ShoalWsSecurityConfiguration.class, ShoalWsRedisSessionConfiguration.class})
public class ShoalWsConfiguration {
}