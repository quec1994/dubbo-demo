package com.demo.provider.manager.dubbo;

import com.demo.DemoService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CompletableFuture;

@DubboService(version = "async")
public class AsyncDemoDubboService implements DemoService {

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
    public CompletableFuture<String> sayHelloAsync(String name) {
        System.out.println("执行了异步服务" + name);
        RpcContext savedContext = RpcContext.getContext();
        return CompletableFuture.supplyAsync(() -> createResult(name, savedContext));
    }

}
