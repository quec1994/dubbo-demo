package com.demo.consumer.single;

import com.demo.consumer.single.base.BaseSingleDubboConsumerDemoTest;
import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;


public class LoadBalanceDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {

    @DubboReference(group ="default", loadbalance = "consistenthash")
    private GreeterService greeterService;

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
            System.out.println(greeterService.sayHello(i % 5 + "世界"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
