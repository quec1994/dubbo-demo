package com.tuling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class DubboConsumerDemo {


//    @Reference(cluster = "failfast", mock = "true")
//    private DemoService demoService;

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(DubboConsumerDemo.class);

//        DemoService demoService = context.getBean(DemoService.class);

        //
//        RpcContext.getContext().setAttachment("dubbo.tag","tag1");

//        System.out.println((demoService.sayHello("周瑜")));

//        RpcContext.getContext().setAttachment("dubbo.tag","tag1");


    }

}
