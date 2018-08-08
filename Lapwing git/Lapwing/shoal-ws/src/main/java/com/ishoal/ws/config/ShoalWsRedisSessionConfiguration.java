package com.ishoal.ws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@Profile("redisSession")
@EnableRedisHttpSession
public class ShoalWsRedisSessionConfiguration { 
    @Value("${server.session-timeout}")
    private int maxInactiveIntervalInSeconds;
}