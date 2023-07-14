package com.demo.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo(scanBasePackages = "com.demo.provider.service")
@PropertySource("classpath:/spring/dubbo-provider.yml")
public class DubboProviderConfiguration {
}
