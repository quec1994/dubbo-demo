package com.demo.single.starter;

import com.demo.consumer.base.config.YamlPropertySourceFactory;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

public class SingleDubboConsumerDemoStarter {
    static Class<?>[] STATER_CLASSES = new Class[]{ApplicationConfiguration.class, DubboConfiguration.class};

    static {
        System.setProperty("dubbo.application.logger", "slf4j");
    }

    public static ConfigurableApplicationContext run(Class<?>... primarySourceClasses) {
        Class<?>[] annotatedClasses = new Class[primarySourceClasses.length + STATER_CLASSES.length];
        System.arraycopy(primarySourceClasses, 0, annotatedClasses, STATER_CLASSES.length, primarySourceClasses.length);
        System.arraycopy(STATER_CLASSES, 0, annotatedClasses, 0, STATER_CLASSES.length);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(annotatedClasses);
        context.start();
        return context;
    }

    @Configuration
    @PropertySource(value = "classpath:/application.yml", factory = YamlPropertySourceFactory.class)
    static class ApplicationConfiguration {

    }

    @Configuration
    @EnableDubbo
    @PropertySource(value = com.demo.consumer.base.config.DubboConfiguration.CONSUMER_PROPERTIES, factory = YamlPropertySourceFactory.class)
    static class DubboConfiguration {

    }

}
