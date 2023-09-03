package com.demo.single;

import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class TimeoutDubboConsumerDemo {


    @DubboReference(group ="timeout")
    private GreeterService greeterService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(TimeoutDubboConsumerDemo.class);

        GreeterService greeterService = context.getBean(GreeterService.class);

        try {
            // 服务调用超时时间为1秒，如果这1秒内没有收到服务结果，则会报错
            System.out.println((greeterService.sayHello("World"))); //xxservestub
        } catch (Exception e) {
            System.out.println("执行出现了异常");
            e.printStackTrace();
        }


    }

}
