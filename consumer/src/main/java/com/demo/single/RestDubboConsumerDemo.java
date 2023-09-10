package com.demo.single;

import com.demo.dubbo.RestGreeterService;
import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.protocol.rest.RestHeaderEnum;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@EnableAutoConfiguration
@Import(RestTemplate.class)
public class RestDubboConsumerDemo {

    @DubboReference(group = "rest")
    private RestGreeterService restGreeterService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(RestDubboConsumerDemo.class);

        // dubboDemo
        RestGreeterService restGreeterService = context.getBean(RestGreeterService.class);
        System.out.println("dubboDemo：" + restGreeterService.sayHello("世界"));

        // httpDemo
        RequestEntity<Void> requestEntity = RequestEntity
                .get("http://localhost:10880/demo/say?name=World")
                .header(RestHeaderEnum.GROUP.getHeader(), "rest")
//                .header(RestHeaderEnum.VERSION.getHeader(), null)
                .build();
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        String result = new String(responseEntity.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        System.out.println("httpDemo：" + result);
    }

}
