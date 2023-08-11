package com.demo.consumer.single;

import com.demo.DemoService;
import com.demo.DemoServiceListener;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashSet;
import java.util.Set;

@EnableAutoConfiguration
public class CallbackDubboConsumerDemo {

    @DubboReference(version = "callback")
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboDemoStarter.run(CallbackDubboConsumerDemo.class);

        DemoService demoService = context.getBean(DemoService.class);

        // 用来进行callback
        DemoServiceListenerImpl listener = new DemoServiceListenerImpl();
        System.out.println(demoService.sayHello("World k1", "k1", listener));
        System.out.println(demoService.sayHello("World k2", "k2", listener));
        System.out.println(demoService.sayHello("World k3", "k3", listener));


        try {
            System.out.println("线程暂停");
            Thread.sleep(5000);
            System.out.println("线程恢复");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listener.destroy("k1");
        listener.destroy("k2");
        listener.destroy("k3");

        while (!listener.destroyKey.isEmpty()) {
            try {
                System.out.println("线程暂停");
                Thread.sleep(1000);
                System.out.println("线程恢复");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static class DemoServiceListenerImpl implements DemoServiceListener {

        private final Set<String> destroyKey = new HashSet<>();

        @Override
        public void changed(String key, String msg) {
            System.out.println("changed 被回调了：" + key + ", " + msg);
        }

        @Override
        public void result(String key, String msg) {
            destroyKey.remove(key);
            System.out.println("result 被回调了：" + key + ", " + msg);
        }

        @Override
        public boolean available(String key) {
            System.out.println("available 被回调了：" + key);
            return !destroyKey.contains(key);
        }

        public void destroy(String key) {
            destroyKey.add(key);
        }

    }
}
