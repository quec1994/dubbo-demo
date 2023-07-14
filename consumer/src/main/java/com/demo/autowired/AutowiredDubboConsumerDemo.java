package com.demo.autowired;

import com.demo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class AutowiredDubboConsumerDemo implements WebMvcConfigurer {

    @DubboReference(version = "default")
    private DemoService demoService;

    public static void main(String[] args) {
        SpringApplication.run(AutowiredDubboConsumerDemo.class);
    }

}
