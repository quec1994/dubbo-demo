package com.demo.consumer.single;

import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import com.demo.consumer.single.base.BaseSingleDubboConsumerDemoTest;

public class DefaultDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {

    @DubboReference(group ="default")
    private GreeterService greeterService;

    @Test
    public void test() {
        System.out.println(greeterService.sayHello("世界"));
    }

}
