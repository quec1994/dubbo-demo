package com.demo.provider.openapi.dubbo.greeter;

import com.demo.common.assertion.ParameterAssert;
import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import com.demo.provider.service.greeter.DefaultGreeterService;
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
