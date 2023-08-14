package com.demo.consumer.single;

import com.demo.dubbo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class DefaultDubboConsumerDemo {

    @DubboReference(version = "default")
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboDemoStarter.run(DefaultDubboConsumerDemo.class);
        DemoService demoService = context.getBean(DemoService.class);
        System.out.println(demoService.sayHello("World"));
    }

}
