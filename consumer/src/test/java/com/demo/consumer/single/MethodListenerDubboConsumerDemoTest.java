package com.demo.consumer.single;

import com.demo.consumer.single.base.BaseSingleDubboConsumerDemoTest;
import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

public class MethodListenerDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {

    @DubboReference(group ="default",
//    @DubboReference(group ="timeout", timeout = 3000,
            methods = {
                    @Method(name = "sayHello", oninvoke = "methodInvokeListener.oninvoke",
                            onreturn = "methodInvokeListener.onreturn",
                            onthrow = "methodInvokeListener.onthrow")
            }
    )
    private GreeterService greeterService;

    @Test
    public void test() {
        System.out.println(greeterService.sayHello("世界"));
    }

    @Bean
    public MethodInvokeListener methodInvokeListener() {
        return new MethodInvokeListener(5000);
    }

    public static class MethodInvokeListener {

        private final ThreadLocal<Long> startTime = new ThreadLocal<>();

        private final int sleepMillis;

        public MethodInvokeListener() {
            sleepMillis = 0;
        }

        public MethodInvokeListener(int sleepMillis) {
            this.sleepMillis = sleepMillis;
        }

        public void oninvoke(String name) {
            startTime.set(System.currentTimeMillis());
            System.out.println("sayHello oninvoke: " + name);
            sleep(sleepMillis);
        }

        public void onreturn(String result, String name) {
            long interval = System.currentTimeMillis() - startTime.get();
            System.out.println("sayHello onreturn: " + interval + ", " + name + ", " + result);
            sleep(sleepMillis);
        }

        public void onthrow(Exception exception, String name) {
            long interval = System.currentTimeMillis() - startTime.get();
            System.out.println("sayHello onthrow: " + interval + ", " + name + ", " + exception.getMessage());
            sleep(sleepMillis);
        }

        private void sleep(int sleepMillis) {
            if (sleepMillis <= 0) {
                return;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(sleepMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
