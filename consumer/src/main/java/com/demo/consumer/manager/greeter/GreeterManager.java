package com.demo.consumer.manager.greeter;

import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GreeterManager {

    @Autowired
    private GreeterDubboService greeterDubboService;

    public String sayHello(String name) {
        return greeterDubboService.sayHello(name);
    }

}
