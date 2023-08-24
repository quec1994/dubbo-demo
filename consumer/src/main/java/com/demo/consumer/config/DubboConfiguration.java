package com.demo.consumer.config;

import com.demo.dubbo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo
@PropertySource("classpath:/dubbo/dubbo-consumer.properties")
public class DubboConfiguration {

    @DubboReference(version = "default")
    private DemoService demoService;

}
