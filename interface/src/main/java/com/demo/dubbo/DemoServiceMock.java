package com.demo.dubbo;

public class DemoServiceMock implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println("DemoServiceMock：" + name + " 执行了 sayHello");
        return "DemoServiceMock：出现Rpc异常，进行了mock";
    }

}
