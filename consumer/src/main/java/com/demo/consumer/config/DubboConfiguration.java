package com.demo.consumer.config;

import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo
@PropertySource(value = DubboConfiguration.CONSUMER_PROPERTIES, factory = YamlPropertySourceFactory.class)
public class DubboConfiguration {

    public static final String CONSUMER_PROPERTIES = "classpath:/dubbo/dubbo-consumer.yml";

    @DubboReference(group ="default")
    private GreeterService greeterService;

}
