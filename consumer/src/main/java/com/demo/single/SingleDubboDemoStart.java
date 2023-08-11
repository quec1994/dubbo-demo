package com.demo.single;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

public class SingleDubboDemoStart {

    public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class, primarySource);
        context.start();
        return context;
    }

    @Configuration
    @EnableDubbo
    @PropertySource(value = {"classpath:/application.properties", "classpath:/spring/dubbo-consumer.properties"})
    static class ConsumerConfiguration {

    }

}
