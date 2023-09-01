package com.demo.single;

import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class DefaultDubboConsumerDemo {

    @DubboReference(version = "default")
    private GreeterService greeterService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(DefaultDubboConsumerDemo.class);
        GreeterService greeterService = context.getBean(GreeterService.class);
        System.out.println(greeterService.sayHello("World"));
    }

}
