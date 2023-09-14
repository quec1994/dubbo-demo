package com.demo.consumer.service.greeter;

import com.demo.consumer.manager.greeter.ProviderGreeterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreeterService {

    @Autowired
    private ProviderGreeterManager providerGreeterManager;

    public String sayHello(String name) {
        return providerGreeterManager.sayHello(name);
    }
}
