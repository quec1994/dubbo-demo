package com.demo.consumer.single;

import com.demo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.CompletableFuture;

@EnableAutoConfiguration
public class AsyncDubboConsumerDemo {

    @DubboReference(version = "async")
    private DemoService demoService;

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SingleDubboDemoStarter.run(AsyncDubboConsumerDemo.class);

        DemoService demoService = context.getBean(DemoService.class);

        // 调用直接返回CompletableFuture
        CompletableFuture<String> future = demoService.sayHelloAsync("World");  // 5

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
