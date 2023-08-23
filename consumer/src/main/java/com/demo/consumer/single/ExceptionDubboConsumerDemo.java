package com.demo.consumer.single;

import com.demo.dubbo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
public class ExceptionDubboConsumerDemo {

    @DubboReference(version = "bizException",
//    @DubboReference(version = "bizException", mock = "fail: return mock-return-123",
//    @DubboReference(version = "timeout", mock = "true",
//    @DubboReference(version = "timeout", mock = "fail: return mock-return-123",
            methods = {
                    @Method(name = "sayHello", oninvoke = "methodInvokeListener.oninvoke",
                            onreturn = "methodInvokeListener.onreturn",
                            onthrow = "methodInvokeListener.onthrow")
            }
    )
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboDemoStarter.run(ExceptionDubboConsumerDemo.class);

        DemoService demoService = context.getBean(DemoService.class);

        System.out.println(demoService.sayHello("customize"));
//        System.out.println(demoService.sayHello("null"));
        // mock可以控制远端方法抛出的RpcException在消费者端是直接抛出还是容错，默认是直接抛出
//        System.out.println(demoService.sayHello("rpc"));
//        System.out.println(demoService.sayHello("rpc_biz"));
//        System.out.println(demoService.sayHello("World"));
    }

    @Bean
    public MethodListenerDubboConsumerDemo.MethodInvokeListener methodInvokeListener() {
        return new MethodListenerDubboConsumerDemo.MethodInvokeListener();
    }

}
