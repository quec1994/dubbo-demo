package com.demo.consumer.manager.greeter;

import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderGreeterManager {

    @Autowired
    private GreeterDubboService defaultGreeterDubboService;

    public String sayHello(String name) {
        return defaultGreeterDubboService.sayHello(name);
    }

}
