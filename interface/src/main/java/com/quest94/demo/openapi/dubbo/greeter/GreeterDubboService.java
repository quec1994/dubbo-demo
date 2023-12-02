package com.quest94.demo.openapi.dubbo.greeter;


import java.util.concurrent.CompletableFuture;

public interface GreeterDubboService {

    // 同步调用方法
    String sayHello(String name);

    // 异步调用方法
    default CompletableFuture<String> sayHelloFuture(String name) {
        return null;
    }

    // 添加回调
    default String sayHello(String name, String key, GreeterDubboServiceCallback listener) {
        return null;
    }

}
