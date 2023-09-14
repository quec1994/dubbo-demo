package com.demo.consumer.single.extraapi.provider;

import com.demo.consumer.single.base.BaseDubboConsumerDemoSingleTest;
import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;


public class StubDubboConsumerDemoTest extends BaseDubboConsumerDemoSingleTest {

    @DubboReference(group ="timeout", timeout = 1000,
            // 使用com.demo.dubbo.DemoServiceStub实现类的方法
//            stub = "true"
            // 使用InternalDemoServiceStub实现类的方法
            stub = "com.demo.single.local.single.LocalDemoServiceStub"
    )
    private GreeterDubboService greeterDubboService;

    @Test
    public void test() {

        System.out.println(greeterDubboService.sayHello("世界"));

    }

}

