package com.demo.single;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class GenericDubboConsumerDemo {


    @DubboReference(id = "demoService", version = "default", interfaceName = "com.demo.DemoService", generic = true)
    private GenericService genericService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboDemoStart.run(GenericDubboConsumerDemo.class);

        GenericService genericService = (GenericService) context.getBean("demoService");

        Object result = genericService.$invoke("sayHello", new String[]{"java.lang.String"}, new Object[]{"World"});
        System.out.println(result);


    }

}
