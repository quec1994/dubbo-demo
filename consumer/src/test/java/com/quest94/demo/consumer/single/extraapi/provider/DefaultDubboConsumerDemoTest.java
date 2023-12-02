package com.quest94.demo.consumer.single.extraapi.provider;

import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboService;
import com.quest94.demo.consumer.single.extraapi.provider.base.BaseProviderDubboConsumerDemoSingleTest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;

public class DefaultDubboConsumerDemoTest extends BaseProviderDubboConsumerDemoSingleTest {

    @DubboReference(group ="default")
    private GreeterDubboService greeterDubboService;

    @Test
    public void test() {
        System.out.println(greeterDubboService.sayHello("世界"));
    }

}
