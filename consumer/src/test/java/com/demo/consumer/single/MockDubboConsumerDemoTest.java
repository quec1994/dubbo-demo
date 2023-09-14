package com.demo.consumer.single;

import com.demo.consumer.single.base.BaseSingleDubboConsumerDemoTest;
import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;


public class MockDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {

    @DubboReference(group ="mock",
            methods = {
                    @Method(name = "sayHello", oninvoke = "methodInvokeListener.oninvoke",
                            onreturn = "methodInvokeListener.onreturn",
                            onthrow = "methodInvokeListener.onthrow")
            }
            // 使用InternalDemoServiceMock实现类的方法返回模拟数据
//            ,mock = "com.demo.single.MockDubboConsumerDemoTest$InternalDemoServiceMock"
            // 返回固定字符串“mock-return-123”
//            ,mock = "fail: return mock-return-123"
            // 使用internalDemoServiceMock Bean的方法返回模拟数据
//            , id = "demoService", mock = "internalDemoServiceMock"
    )
    private GreeterService greeterService;

    @Test
    public void test() {
        System.out.println(greeterService.sayHello("世界"));
    }

    @Bean
    public MethodListenerDubboConsumerDemoTest.MethodInvokeListener methodInvokeListener() {
        return new MethodListenerDubboConsumerDemoTest.MethodInvokeListener();
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
