package com.demo.consumer.single.extraapi.provider;

import com.demo.consumer.single.base.BaseDubboConsumerDemoSingleTest;
import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;

public class BigDataDubboConsumerDemoTest extends BaseDubboConsumerDemoSingleTest {

    //    @DubboReference(group ="bigData", protocol = CommonConstants.DUBBO)
    @DubboReference(group ="bigData", protocol = CommonConstants.TRIPLE)
    private GreeterDubboService greeterDubboService;

    @Test
    public void test() {
        long st = System.currentTimeMillis();
        String reply = greeterDubboService.sayHello("World");
        // 5MB response
        System.out.println("Reply length:" + reply.length() + " cost:" + (System.currentTimeMillis() - st));
    }

}
