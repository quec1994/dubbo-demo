package com.demo.single;

import com.demo.dubbo.GreeterService;
import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@Configuration
public class MockDubboConsumerDemo {

    @DubboReference(group ="mock",
            methods = {
                    @Method(name = "sayHello", oninvoke = "methodInvokeListener.oninvoke",
                            onreturn = "methodInvokeListener.onreturn",
                            onthrow = "methodInvokeListener.onthrow")
            }
            // 使用InternalDemoServiceMock实现类的方法返回模拟数据
//            ,mock = "com.demo.single.MockDubboConsumerDemo$InternalDemoServiceMock"
            // 返回固定字符串“mock-return-123”
//            ,mock = "fail: return mock-return-123"
            // 使用internalDemoServiceMock Bean的方法返回模拟数据
//            , id = "demoService", mock = "internalDemoServiceMock"
    )
    private GreeterService greeterService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(MockDubboConsumerDemo.class);

        GreeterService greeterService = context.getBean(GreeterService.class);
//        DemoService demoService = context.getBean("demoService", DemoService.class);
        System.out.println(greeterService.sayHello("世界"));
    }

    @Bean
    public MethodListenerDubboConsumerDemo.MethodInvokeListener methodInvokeListener() {
        return new MethodListenerDubboConsumerDemo.MethodInvokeListener();
    }

//    @Bean
    public GreeterService internalDemoServiceMock() {
        return new InternalGreeterServiceMock();
    }

    public static class InternalGreeterServiceMock implements GreeterService {

        @Override
        public String sayHello(String name) {
            System.out.println("InternalDemoServiceMock：" + name + " 执行了 sayHello");
            return "InternalDemoServiceMock：出现Rpc异常，进行了mock";
        }

    }

}
