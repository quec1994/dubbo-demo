package com.demo.single;

import com.demo.dubbo.RestDemoService;
import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@EnableAutoConfiguration
@Import(RestTemplate.class)
public class RestDubboConsumerDemo {

    @DubboReference(version = "rest")
    private RestDemoService restDemoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(RestDubboConsumerDemo.class);

        // dubboDemo
        RestDemoService restDemoService = context.getBean(RestDemoService.class);
        System.out.println("dubboDemo：" + restDemoService.sayHello("World"));

        // httpDemo
        RequestEntity<Void> requestEntity = RequestEntity
                .get(URI.create("http://localhost:10880/demo/say?name=World"))
                .build();
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        System.out.println("httpDemo：" + responseEntity.getBody());
    }

}
