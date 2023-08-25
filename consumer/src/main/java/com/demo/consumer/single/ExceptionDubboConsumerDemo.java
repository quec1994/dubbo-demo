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

    @DubboReference(version = "exception", mock = "fail: return mock-return-123",
//    @DubboReference(version = "exception",
            methods = {
                    @Method(name = "sayHello", oninvoke = "methodInvokeListener.oninvoke",
                            onreturn = "methodInvokeListener.onreturn",
                            onthrow = "methodInvokeListener.onthrow")
            }
    )
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboDemoStarter.run(ExceptionDubboConsumerDemo.class);

        try {
            DemoService demoService = context.getBean(DemoService.class);
            // 不会触发集群容错，也不会触发mock容错
//            System.out.println(demoService.sayHello("customize"));
            // 不会触发集群容错，也不会触发mock容错
//        System.out.println(demoService.sayHello("null"));
            // 不会触发集群容错，也不会触发mock容错
//        System.out.println(demoService.sayHello("rpc_biz"));
            // 不会触发集群容错，但是可以触发mock容错
        System.out.println(demoService.sayHello("rpc"));
        } catch (Exception e) {
            System.out.println("执行时抛出了异常");
            e.printStackTrace();
        }
        System.out.println("执行结束");
    }

    @Bean
    public MethodListenerDubboConsumerDemo.MethodInvokeListener methodInvokeListener() {
        return new MethodListenerDubboConsumerDemo.MethodInvokeListener();
    }

}
