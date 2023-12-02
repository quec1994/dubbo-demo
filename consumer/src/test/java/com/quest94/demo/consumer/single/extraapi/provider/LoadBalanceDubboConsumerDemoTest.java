package com.quest94.demo.consumer.single.extraapi.provider;

import com.quest94.demo.consumer.single.extraapi.provider.base.BaseProviderDubboConsumerDemoSingleTest;
import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;


public class LoadBalanceDubboConsumerDemoTest extends BaseProviderDubboConsumerDemoSingleTest {

    @DubboReference(group ="default", loadbalance = "consistenthash")
    private GreeterDubboService greeterDubboService;

    @Test
    public void test() {

        // 用来负载均衡
//        for (int i = 0; i < 1000; i++) {
//            System.out.println(demoService.sayHello("世界"));
//            try {
//                Thread.sleep(1 * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        // 一致性hash算法测试
        for (int i = 0; i < 1000; i++) {
            System.out.println(greeterDubboService.sayHello(i % 5 + "世界"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
