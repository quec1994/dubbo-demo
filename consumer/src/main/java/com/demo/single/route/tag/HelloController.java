package com.demo.single.route.tag;

import com.demo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @DubboReference(version = "default")
    private DemoService demoService;

    @RequestMapping("/consumer/user/say")
    public String sayHello() {
        return demoService.sayHello("WorldController");
    }

}
