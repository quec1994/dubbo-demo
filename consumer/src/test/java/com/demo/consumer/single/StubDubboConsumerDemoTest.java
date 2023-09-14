package com.demo.consumer.single;

import com.demo.consumer.single.base.BaseSingleDubboConsumerDemoTest;
import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;


public class StubDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {

    @DubboReference(group ="timeout", timeout = 1000,
            // 使用com.demo.dubbo.DemoServiceStub实现类的方法
//            stub = "true"
            // 使用InternalDemoServiceStub实现类的方法
            stub = "com.demo.single.local.single.LocalDemoServiceStub"
    )
    private GreeterService greeterService;

    @Test
    public void test() {

        System.out.println(greeterService.sayHello("世界"));

    }

}

