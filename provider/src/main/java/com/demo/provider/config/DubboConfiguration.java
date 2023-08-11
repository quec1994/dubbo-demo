package com.demo.provider.config;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo
@DubboComponentScan(basePackages = "com.demo.provider.manager.dubbo")
@PropertySource("classpath:/spring/dubbo-provider.properties")
public class DubboConfiguration {
}
