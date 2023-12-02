package com.quest94.demo.consumer.single.extraapi.provider;

import com.quest94.demo.consumer.single.extraapi.provider.base.BaseProviderDubboConsumerDemoSingleTest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.service.GenericService;
import org.junit.jupiter.api.Test;


public class GenericDubboConsumerDemoTest extends BaseProviderDubboConsumerDemoSingleTest {

    @DubboReference(interfaceName = "com.demo.openapi.dubbo.GreeterDubboService", group = "generic")
    private GenericService genericService;

    @Test
    public void test() {

        Object result = genericService.$invoke("sayHello", new String[]{"java.lang.String"}, new Object[]{"世界"});
        System.out.println(result);

    }

}
