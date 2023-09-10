package com.demo.single;

import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
public class ClusterDubboConsumerDemo {


    @DubboReference(group ="timeout", cluster = "failfast",
//    @DubboReference(group ="timeout", cluster = "failover",
            methods = {
                    @Method(name = "sayHello", oninvoke = "methodInvokeListener.oninvoke",
                            onreturn = "methodInvokeListener.onreturn",
                            onthrow = "methodInvokeListener.onthrow")
            }
    )
    private GreeterService greeterService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(ClusterDubboConsumerDemo.class);

        GreeterService greeterService = context.getBean(GreeterService.class);

        System.out.println((greeterService.sayHello("世界")));

    }

    @Bean
    public MethodListenerDubboConsumerDemo.MethodInvokeListener methodInvokeListener() {
        return new MethodListenerDubboConsumerDemo.MethodInvokeListener();
    }

}
