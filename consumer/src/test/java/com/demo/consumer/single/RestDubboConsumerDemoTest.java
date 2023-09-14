package com.demo.consumer.single;

import com.demo.consumer.single.base.BaseSingleDubboConsumerDemoTest;
import com.demo.openapi.dubbo.greeter.GreeterDubboRestService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.protocol.rest.RestHeaderEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;


@Import(RestTemplate.class)
public class RestDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {

    @DubboReference(group = "rest")
    private GreeterDubboRestService greeterDubboRestService;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test() {
        // dubboDemo
        System.out.println("dubboDemo：" + greeterDubboRestService.sayHello("世界"));

        // httpDemo
        RequestEntity<Void> requestEntity = RequestEntity
                .get("http://localhost:10880/demo/say?name=World")
                .header(RestHeaderEnum.GROUP.getHeader(), "rest")
//                .header(RestHeaderEnum.VERSION.getHeader(), null)
                .build();
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        String result = new String(responseEntity.getBody().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        System.out.println("httpDemo：" + result);
    }

}
