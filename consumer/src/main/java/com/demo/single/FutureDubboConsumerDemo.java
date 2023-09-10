package com.demo.single;

import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.CompletableFuture;

@EnableAutoConfiguration
public class FutureDubboConsumerDemo {

    @DubboReference(group ="future", timeout = 3000)
    private GreeterService greeterService;

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(FutureDubboConsumerDemo.class);

        GreeterService greeterService = context.getBean(GreeterService.class);

        // 调用直接返回CompletableFuture
        CompletableFuture<String> future = greeterService.sayHelloFuture("世界");  // 5

        future.whenComplete((v, t) -> {
            if (t != null) {
                t.printStackTrace();
            } else {
                System.out.println("Response: " + v);
            }
        });

        try {
            System.out.println("线程暂停");
            Thread.sleep(3000);
            System.out.println("线程恢复");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
