package com.quest94.demo.provider.service.greeter;

import org.springframework.stereotype.Service;


@Service
public class DefaultGreeterService {

    public String sayHello(String name) {
        System.out.println(name + " 执行了default服务");
        return String.format("您好 %s！", name);
    }

}
