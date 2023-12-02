package com.quest94.demo.provider.openapi.dubbo.greeter;

import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboService;
import com.quest94.demo.provider.common.assertion.ParameterAssert;
import com.quest94.demo.provider.service.greeter.DefaultGreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;


@DubboService(group ="default")
public class DefaultGreeterDubboServiceImpl implements GreeterDubboService {

    @Autowired
    private DefaultGreeterService defaultGreeterService;

    @Override
    public String sayHello(String name) {
        ParameterAssert.hasText(name, "name");
        System.out.println(name + " 执行了default服务");
        return defaultGreeterService.sayHello(name);
    }


}
