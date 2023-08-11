package com.demo.consumer.single.route.tag;

import com.demo.DemoService;
import com.demo.consumer.single.SingleDubboDemoStarter;
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
        ConfigurableApplicationContext context = SingleDubboDemoStarter.run(TagDubboConsumerDemo.class);

        DemoService demoService = context.getBean(DemoService.class);
        System.out.println((demoService.sayHello("World")));
    }

}
