package com.tuling;

import com.tuling.controller.ConsumerInterceptor;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@SpringBootApplication
public class DubboConsumerDemo implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ConsumerInterceptor());
    }

    @Reference
    private DemoService demoService;

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(DubboConsumerDemo.class);

        System.out.println(context);

//        DemoService demoService = context.getBean(DemoService.class);

        //
//        RpcContext.getContext().setAttachment("dubbo.tag","tag1");

//        System.out.println((demoService.sayHello("周瑜")));

//        RpcContext.getContext().setAttachment("dubbo.tag","tag1");


    }

}
