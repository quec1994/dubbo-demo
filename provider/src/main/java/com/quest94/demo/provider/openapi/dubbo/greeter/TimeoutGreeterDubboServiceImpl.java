package com.quest94.demo.provider.openapi.dubbo.greeter;

import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboService;
import com.quest94.demo.provider.common.assertion.ParameterAssert;
import com.quest94.demo.provider.service.greeter.TimeoutGreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;


@DubboService(group ="timeout", timeout = 1000, parameters = "enable-timeout-countdown:true")
public class TimeoutGreeterDubboServiceImpl implements GreeterDubboService {

    @Autowired
    private TimeoutGreeterService timeoutGreeterService;

    @Override
    public String sayHello(String name) {
        ParameterAssert.hasText(name, "name");
        System.out.println(name + " 执行了timeout服务");
        return timeoutGreeterService.sayHello(name);
    }

}
