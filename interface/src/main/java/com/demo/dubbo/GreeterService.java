package com.demo.dubbo;


import java.util.concurrent.CompletableFuture;

public interface GreeterService {

    // 同步调用方法
    String sayHello(String name);

    // 异步调用方法
    default CompletableFuture<String> sayHelloFuture(String name) {
        return null;
    }

    // 添加回调
    default String sayHello(String name, String key, GreeterServiceCallback listener) {
        return null;
    }

}
