package com.demo.config;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo
@DubboComponentScan(basePackages = "com.demo.provider.service")
@PropertySource("classpath:/spring/dubbo-provider.properties")
public class DubboProviderConfiguration {
}
