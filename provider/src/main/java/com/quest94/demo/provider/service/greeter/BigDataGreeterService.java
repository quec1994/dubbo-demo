package com.quest94.demo.provider.service.greeter;

import org.springframework.stereotype.Service;

@Service
public class BigDataGreeterService {

    public String sayHello(String name) {
        StringBuilder responseBuilder = new StringBuilder(name);
        for (int i = 0; i < 20; i++) {
            responseBuilder.append(responseBuilder);
        }
        return responseBuilder.toString();
    }

}
