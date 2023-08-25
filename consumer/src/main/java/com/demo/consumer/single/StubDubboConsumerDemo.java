package com.demo.consumer.single;

import com.demo.consumer.single.starter.SingleDubboConsumerDemoStarter;
import com.demo.dubbo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class StubDubboConsumerDemo {

    @DubboReference(version = "timeout", timeout = 1000,
            // 使用com.demo.dubbo.DemoServiceStub实现类的方法
//            stub = "true"
            // 使用InternalDemoServiceStub实现类的方法
            stub = "com.demo.consumer.single.local.LocalDemoServiceStub"
    )
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(StubDubboConsumerDemo.class);

        DemoService demoService = context.getBean(DemoService.class);

        System.out.println((demoService.sayHello("World")));


    }

}

