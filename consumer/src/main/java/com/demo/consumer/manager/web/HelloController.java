package com.demo.consumer.manager.web;

import com.demo.dubbo.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private DemoService demoService;

    @RequestMapping("/consumer/user/say")
    public String sayHello() {
        return demoService.sayHello("World");
    }

}
