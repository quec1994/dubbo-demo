package com.demo.consumer.manager.web;

import com.demo.dubbo.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("salute")
public class SaluteController {

    @Autowired
    private DemoService demoService;

    @GetMapping("sayHello")
    public String sayHello(String name) {
        return demoService.sayHello(name);
    }

}
