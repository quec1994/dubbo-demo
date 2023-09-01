package com.demo.consumer.manager.web;

import com.demo.dubbo.GreeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("greeter")
public class GreeterController {

    @Autowired
    private GreeterService greeterService;

    @GetMapping("sayHello")
    public String sayHello(String name) {
        return greeterService.sayHello(name);
    }

}
