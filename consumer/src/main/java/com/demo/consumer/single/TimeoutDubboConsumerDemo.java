package com.demo.consumer.single;

import com.demo.dubbo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class TimeoutDubboConsumerDemo {


    @DubboReference(version = "timeout", timeout = 3000)
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboDemoStarter.run(TimeoutDubboConsumerDemo.class);

        DemoService demoService = context.getBean(DemoService.class);

        // 服务调用超时时间为1秒，默认为3秒
        // 如果这1秒内没有收到服务结果，则会报错
        System.out.println((demoService.sayHello("World"))); //xxservestub


    }

}
