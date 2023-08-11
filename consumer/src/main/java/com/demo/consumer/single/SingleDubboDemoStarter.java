package com.demo.consumer.single;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

public class SingleDubboDemoStarter {

    public static ConfigurableApplicationContext run(Class<?> primarySource) {
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
