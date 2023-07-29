package com.demo.config;

import com.demo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo
@PropertySource("classpath:/spring/dubbo-consumer.properties")
public class DubboConfig {

    @DubboReference(version = "default")
    private DemoService demoService;

}
