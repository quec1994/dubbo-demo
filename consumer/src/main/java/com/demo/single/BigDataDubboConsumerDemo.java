package com.demo.single;

import com.demo.dubbo.GreeterService;
import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@EnableAutoConfiguration
public class BigDataDubboConsumerDemo {

    //    @DubboReference(group ="bigData", protocol = CommonConstants.DUBBO)
    @DubboReference(group ="bigData", protocol = CommonConstants.TRIPLE)
    private GreeterService greeterService;

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(BigDataDubboConsumerDemo.class);
        final GreeterService greeterWrapperService = context.getBean(GreeterService.class);
        long st = System.currentTimeMillis();
        String reply = greeterWrapperService.sayHello("World");
        // 5MB response
        System.out.println("Reply length:" + reply.length() + " cost:" + (System.currentTimeMillis() - st));
    }
}
