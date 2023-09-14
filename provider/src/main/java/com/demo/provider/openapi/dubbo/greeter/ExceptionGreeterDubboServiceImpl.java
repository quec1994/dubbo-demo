package com.demo.provider.openapi.dubbo.greeter;

import com.demo.common.assertion.ParameterAssert;
import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import com.demo.provider.service.greeter.ExceptionGreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;


@DubboService(group ="exception")
public class ExceptionGreeterDubboServiceImpl implements GreeterDubboService {

    @Autowired
    private ExceptionGreeterService exceptionGreeterService;

    @Override
    public String sayHello(String name) {
        ParameterAssert.hasText(name, "name");
        System.out.println(name + " 执行了exception服务");
        return exceptionGreeterService.sayHello(name);
    }

}
