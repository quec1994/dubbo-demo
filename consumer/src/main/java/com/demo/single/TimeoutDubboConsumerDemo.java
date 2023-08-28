package com.demo.single;

import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import com.demo.dubbo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class TimeoutDubboConsumerDemo {


    @DubboReference(version = "timeout", timeout = 1000)
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(TimeoutDubboConsumerDemo.class);

        DemoService demoService = context.getBean(DemoService.class);

        try {
            // 服务调用默认超时时间为1秒，如果这1秒内没有收到服务结果，则会报错
            System.out.println((demoService.sayHello("World"))); //xxservestub
        } catch (Exception e) {
            System.out.println("执行出现了异常");
            e.printStackTrace();
        }


    }

}
