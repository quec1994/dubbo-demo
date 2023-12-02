package com.quest94.demo.provider.service.greeter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.dubbo.rpc.AsyncContext;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class FutureGreeterService {

    @Autowired
    private DefaultGreeterService defaultGreeterService;

    public String sayHello(String name) {
        return defaultGreeterService.sayHello(name);
    }

    public CompletableFuture<String> sayHelloFuture(String name) {
        final AsyncContext asyncContext = RpcContext.startAsync();
        return CompletableFuture.supplyAsync(() -> {
            // 如果要使用上下文，则必须要放在第一句执行
            asyncContext.signalContextSwitch();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return defaultGreeterService.sayHello(name);
        });
    }

}
