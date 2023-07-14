package com.tuling.consumer;

import com.tuling.DemoService;
import com.tuling.DemoServiceListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@EnableAutoConfiguration
public class CallbackDubboConsumerDemo {


    @DubboReference(version = "callback")
    private DemoService demoService;

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(CallbackDubboConsumerDemo.class);

        DemoService demoService = context.getBean(DemoService.class);

        // 用来进行callback
        System.out.println(demoService.sayHello("周瑜", "d1", new DemoServiceListenerImpl()));
        System.out.println(demoService.sayHello("周瑜", "d2", new DemoServiceListenerImpl()));
        System.out.println(demoService.sayHello("周瑜", "d3", new DemoServiceListenerImpl()));
    }

    static class DemoServiceListenerImpl implements DemoServiceListener {

        @Override
        public void changed(String msg) {
            System.out.println("被回调了：" + msg);
        }
    }
}
