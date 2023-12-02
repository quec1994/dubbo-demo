package com.quest94.demo.openapi.dubbo.greeter;

public class GreeterDubboServiceMock implements GreeterDubboService {

    @Override
    public String sayHello(String name) {
        System.out.println("DemoServiceMock：" + name + " 执行了 sayHello");
        return "DemoServiceMock：出现Rpc异常，进行了mock";
    }

}
