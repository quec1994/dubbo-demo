package com.demo.consumer.single.extraapi.provider;

import com.demo.consumer.single.base.BaseDubboConsumerDemoSingleTest;
import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

public class ExceptionDubboConsumerDemoTest extends BaseDubboConsumerDemoSingleTest {

    @DubboReference(group ="exception", mock = "fail: return mock-return-123",
//    @DubboReference(group ="exception",
            methods = {
                    @Method(name = "sayHello", oninvoke = "methodInvokeListener.oninvoke",
                            onreturn = "methodInvokeListener.onreturn",
                            onthrow = "methodInvokeListener.onthrow")
            }
    )
    private GreeterDubboService greeterDubboService;

    @Test
    public void test() {
        try {
            // 不会触发集群容错，也不会触发mock容错
//            System.out.println(demoService.sayHello("customize"));
            // 不会触发集群容错，也不会触发mock容错
//        System.out.println(demoService.sayHello("null"));
            // 不会触发集群容错，也不会触发mock容错
//        System.out.println(demoService.sayHello("rpc_biz"));
            // 不会触发集群容错，但是可以触发mock容错
        System.out.println(greeterDubboService.sayHello("rpc"));
        } catch (Exception e) {
            System.out.println("执行时抛出了异常");
            e.printStackTrace();
        }
        System.out.println("执行结束");
    }

    @Bean
    public MethodListenerDubboConsumerDemoTest.MethodInvokeListener methodInvokeListener() {
        return new MethodListenerDubboConsumerDemoTest.MethodInvokeListener();
    }

}
