package com.demo.consumer.single;

import com.demo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
public class MethodListenerDubboConsumerDemo {

    @DubboReference(version = "default",
//    @DubboReference(version = "timeout", timeout = 3000,
            methods = {
                    @Method(name = "sayHello", oninvoke = "methodInvokeListener.oninvoke", onreturn = "methodInvokeListener.onreturn",
                            onthrow = "methodInvokeListener.onthrow")
            }
    )
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboDemoStarter.run(MethodListenerDubboConsumerDemo.class);
        DemoService demoService = context.getBean(DemoService.class);
        System.out.println(demoService.sayHello("World"));
    }

    @Bean
    public MethodInvokeListener methodInvokeListener() {
        return new MethodInvokeListener();
    }

    public static class MethodInvokeListener {

        public void oninvoke(String name) throws InterruptedException {
            System.out.println("sayHello oninvoke " + name);
            Thread.sleep(5000);
        }

        public void onreturn(String result, String name) throws InterruptedException {
            System.out.println("sayHello onreturn " + name + " " + result);
            Thread.sleep(5000);
        }

        public void onthrow(Exception exception, String name) throws InterruptedException {
            System.out.println("sayHello onthrow " + name + " " + exception.getMessage());
            exception.printStackTrace();
            Thread.sleep(5000);
        }

    }

}
