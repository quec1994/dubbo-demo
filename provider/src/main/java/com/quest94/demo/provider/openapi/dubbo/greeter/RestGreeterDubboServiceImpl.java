package com.quest94.demo.provider.openapi.dubbo.greeter;

import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboRestService;
import com.quest94.demo.provider.common.assertion.ParameterAssert;
import com.quest94.demo.provider.service.greeter.DefaultGreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;


@DubboService(group ="rest", protocol = "rest")
public class RestGreeterDubboServiceImpl implements GreeterDubboRestService {

    @Autowired
    private DefaultGreeterService defaultGreeterService;

    @Override
    public String sayHello(String name) {
        ParameterAssert.hasText(name, "name");
        System.out.println(name + " 执行了rest服务");
        return defaultGreeterService.sayHello(name);
    }

}
