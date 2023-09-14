package com.demo.provider.openapi.dubbo.greeter;

import com.demo.openapi.dubbo.greeter.GreeterDubboRestService;
import com.demo.provider.common.assertion.ParameterAssert;
import com.demo.provider.service.greeter.DefaultGreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;


@DubboService(group ="rest", protocol = "rest")
public class GreeterDubboRestServiceImpl implements GreeterDubboRestService {

    @Autowired
    private DefaultGreeterService defaultGreeterService;

    @Override
    public String sayHello(String name) {
        ParameterAssert.hasText(name, "name");
        System.out.println(name + " 执行了rest服务");
        return defaultGreeterService.sayHello(name);
    }

}
