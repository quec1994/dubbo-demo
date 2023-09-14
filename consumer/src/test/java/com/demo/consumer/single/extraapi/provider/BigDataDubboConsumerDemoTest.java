package com.demo.consumer.single.extraapi.provider;

import com.demo.consumer.single.extraapi.provider.base.BaseProviderDubboConsumerDemoSingleTest;
import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BigDataDubboConsumerDemoTest extends BaseProviderDubboConsumerDemoSingleTest {

    @DubboReference(group = "bigData", protocol = CommonConstants.DUBBO)
    private GreeterDubboService dubboGreeterDubboService;
    @DubboReference(group = "bigData", protocol = CommonConstants.TRIPLE)
    private GreeterDubboService tripleGreeterDubboService;

    @Test
    @Order(0)
    public void preheat() {
        testHandle(tripleGreeterDubboService, "preheatTriple");
        testHandle(dubboGreeterDubboService, "preheatDubbo");
    }

    @Test
    @Order(1)
    public void testTripleProtocol() {
        testHandle(tripleGreeterDubboService, "triple");
    }

    @Test
    @Order(1)
    public void testDubboProtocol() {
        testHandle(dubboGreeterDubboService, "dubbo");
    }

    private void testHandle(GreeterDubboService greeterDubboService, String methodFlag) {
        long st = System.currentTimeMillis();
        String reply = greeterDubboService.sayHello("World");
        // 5MB response
        long cost = System.currentTimeMillis() - st;
        System.out.println(methodFlag + "：Reply length:" + reply.length() + " cost:" + cost);
    }

}
