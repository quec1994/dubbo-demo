package com.demo.provider.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo(scanBasePackages = "com.demo.provider.manager.dubbo")
@PropertySource(value = "classpath:/dubbo/dubbo-provider.yml", factory = YamlPropertySourceFactory.class)
public class DubboConfiguration {
}
