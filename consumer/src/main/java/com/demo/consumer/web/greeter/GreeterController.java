package com.demo.consumer.web.greeter;

import com.demo.consumer.common.assertion.ParameterAssert;
import com.demo.consumer.service.greeter.GreeterService;
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
        ParameterAssert.hasText(name, "name");
        return greeterService.sayHello(name);
    }

}
