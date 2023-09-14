package com.demo.consumer.single.base;

import com.demo.consumer.DubboConsumerDemo;
import com.demo.consumer.base.config.YamlPropertySourceFactory;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        // 默认不加载web环境
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                BaseSingleDubboConsumerDemoTest.ApplicationConfiguration.class,
                BaseSingleDubboConsumerDemoTest.DubboConfiguration.class
        })
// 指定测试环境的配置文件
@ActiveProfiles("developer")
public class BaseSingleDubboConsumerDemoTest {

    static class ApplicationConfiguration extends DubboConsumerDemo {

    }

    @Configuration
    @EnableDubbo
    @PropertySource(value = com.demo.consumer.base.config.DubboConfiguration.CONSUMER_PROPERTIES, factory = YamlPropertySourceFactory.class)
    static class DubboConfiguration {

        static {
            System.setProperty("dubbo.application.logger", "slf4j");
        }

    }

}
