package com.demo.consumer.single.extraapi.provider;

import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import com.demo.consumer.single.extraapi.provider.base.BaseProviderDubboConsumerDemoSingleTest;

public class DefaultDubboConsumerDemoTest extends BaseProviderDubboConsumerDemoSingleTest {

    @DubboReference(group ="default")
    private GreeterDubboService greeterDubboService;

    @Test
    public void test() {
        System.out.println(greeterDubboService.sayHello("世界"));
    }

}
