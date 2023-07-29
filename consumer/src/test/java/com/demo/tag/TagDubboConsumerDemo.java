package com.demo.tag;

import com.demo.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class TagDubboConsumerDemo implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ConsumerInterceptor());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TagDubboConsumerDemo.class);

        DemoService demoService = context.getBean(DemoService.class);
        System.out.println((demoService.sayHello("周瑜")));
    }

}
