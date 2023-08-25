package com.demo.single.local;

import com.demo.dubbo.DemoService;

public class LocalDemoServiceStub implements DemoService {

    private final DemoService demoService;

    // 构造函数传入真正的远程代理对象
    public LocalDemoServiceStub(DemoService demoService) {
        this.demoService = demoService;
    }

    @Override
    public String sayHello(String name) {
        // 此代码在客户端执行, 你可以在客户端做ThreadLocal本地缓存，或预先验证参数是否合法，等等
        try {
            System.out.println("LocalDemoServiceStub：" + name + " 执行了 sayHello");
            return demoService.sayHello(name); // safe  null
        } catch (Exception e) {
            System.out.println("LocalDemoServiceStub：" + name + " 执行 sayHello 时发生了异常");
            // 你可以容错，可以做任何AOP拦截事项
            e.printStackTrace();
            return "LocalDemoServiceStub：容错数据";
        }
    }

}
