package com.demo.provider.manager.dubbo;

import com.demo.dubbo.DemoService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@DubboService(version = "future")
public class FutureDemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println("执行了同步服务" + name);
        return createResult(name, RpcContext.getContext());
    }

    private static String createResult(String name, RpcContext context) {
        URL url = context.getUrl();
        return String.format("%s：%s, Hello, %s", url.getProtocol(), url.getPort(), name);
    }

    @Override
    public CompletableFuture<String> sayHelloFuture(String name) {
        System.out.println("执行了异步服务" + name);
        RpcContext savedContext = RpcContext.getContext();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return CompletableFuture.supplyAsync(() -> createResult(name, savedContext));
    }

}
