package com.demo.single;

import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import com.demo.dubbo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class LoadBalanceDubboConsumerDemo {


    @DubboReference(version = "default", loadbalance = "consistenthash")
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(LoadBalanceDubboConsumerDemo.class);

        DemoService demoService = context.getBean(DemoService.class);

        // 用来负载均衡
//        for (int i = 0; i < 1000; i++) {
//            System.out.println((demoService.sayHello("World")));
//            try {
//                Thread.sleep(1 * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        // 一致性hash算法测试
        for (int i = 0; i < 1000; i++) {
            System.out.println((demoService.sayHello(i % 5 + "World")));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
