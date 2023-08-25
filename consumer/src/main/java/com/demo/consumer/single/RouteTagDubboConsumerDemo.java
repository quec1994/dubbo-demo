package com.demo.consumer.single;

import com.demo.consumer.single.starter.SingleDubboConsumerDemoStarter;
import com.demo.dubbo.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableAutoConfiguration
public class RouteTagDubboConsumerDemo implements WebMvcConfigurer {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(RouteTagDubboConsumerDemo.class,
                RouteTagTestService.class);
        Map<String, String> map = new HashMap<>();
        map.put("phone", "18888888888");
        map.put("name", "World");
        RouteTagTestService routeTagTestService = context.getBean(RouteTagTestService.class);
        System.out.println(routeTagTestService.sayHello(map));
    }

    @Service
    public static class RouteTagTestService {
        // 测试账号
        private static final List<String> tester = new ArrayList<>();

        static {
            tester.add("18888888888");
        }

        @DubboReference(version = "default")
        private DemoService demoService;

        @RequestMapping("/consumer/user/say")
        public String sayHello(Map<String, String> map) {
            map.get("name");
            String iphone = map.get("phone");
            if (tester.contains(iphone)) {
                RpcContext.getContext().setAttachment("dubbo.tag", "tester");
            }
            return demoService.sayHello("World");
        }

    }

}
