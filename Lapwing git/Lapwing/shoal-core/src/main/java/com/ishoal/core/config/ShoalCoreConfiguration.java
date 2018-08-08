package com.ishoal.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ShoalCoreBeanConfiguration.class})
public class ShoalCoreConfiguration {

}
