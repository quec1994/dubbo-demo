package com.demo.consumer.single;

import com.demo.consumer.single.starter.SingleDubboConsumerDemoStarter;
import com.demo.dubbo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@EnableAutoConfiguration
@Configuration
public class MethodListenerDubboConsumerDemo {

    @DubboReference(version = "default",
//    @DubboReference(version = "timeout", timeout = 3000,
            methods = {
                    @Method(name = "sayHello", oninvoke = "methodInvokeListener.oninvoke",
                            onreturn = "methodInvokeListener.onreturn",
                            onthrow = "methodInvokeListener.onthrow")
            }
    )
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(MethodListenerDubboConsumerDemo.class);
        DemoService demoService = context.getBean(DemoService.class);
        System.out.println(demoService.sayHello("World"));
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
