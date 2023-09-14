package com.demo.consumer.single;

import com.demo.consumer.single.base.BaseSingleDubboConsumerDemoTest;
import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RouteTagDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {

    @Autowired
    private RouteTagTestService routeTagTestService;

    @Test
    public void test() {
        Map<String, String> map = new HashMap<>();
        map.put("phone", "18888888888");
        map.put("name", "世界");
        System.out.println(routeTagTestService.sayHello(map));
    }

    @Service
    public static class RouteTagTestService {
        // 测试账号
        private static final List<String> tester = new ArrayList<>();

        static {
            tester.add("18888888888");
        }

        @DubboReference(group ="default")
        private GreeterService greeterService;

        @RequestMapping("/consumer/user/say")
        public String sayHello(Map<String, String> map) {
            map.get("name");
            String iphone = map.get("phone");
            if (tester.contains(iphone)) {
                RpcContext.getServiceContext().setAttachment("dubbo.tag", "tester");
            }
            return greeterService.sayHello("世界");
        }

    }

}
