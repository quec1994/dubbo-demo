package com.demo.consumer.service.greeter;

import com.demo.consumer.manager.greeter.GreeterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreeterService {

    @Autowired
    private GreeterManager greeterManager;

    public String sayHello(String name) {
        return greeterManager.sayHello(name);
    }
}
