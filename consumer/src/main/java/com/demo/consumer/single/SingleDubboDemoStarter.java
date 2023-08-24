package com.demo.consumer.single;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

public class SingleDubboDemoStarter {

    public static ConfigurableApplicationContext run(Class<?>... primarySourceClasses) {
        Class<?>[] annotatedClasses = new Class[primarySourceClasses.length + 1];
        System.arraycopy(primarySourceClasses, 0, annotatedClasses, 1, primarySourceClasses.length);
        annotatedClasses[0] = ConsumerConfiguration.class;
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(annotatedClasses);
        context.start();
        return context;
    }

    @Configuration
    @EnableDubbo
    @PropertySource(value = {"classpath:/application.yml", "classpath:/dubbo/dubbo-consumer.properties"})
    static class ConsumerConfiguration {

    }

}
