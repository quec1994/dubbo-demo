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
        System.out.println(name + " 调用了同步服务");
        return createResult(name);
    }

    private String createResult(String name) {
        URL url = RpcContext.getContext().getUrl();
        return String.format("%s：%s, Hello %s", url.getProtocol(), url.getPort(), name);
    }

    @Override
    public CompletableFuture<String> sayHelloFuture(String name) {
        System.out.println(name + " 执行了异步方法返回值服务");
        RpcContext savedContext = RpcContext.getContext();
        RpcContext savedServerContext = RpcContext.getServerContext();
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            RpcContext.restoreContext(savedContext);
            RpcContext.restoreServerContext(savedServerContext);
            return this.createResult(name);
        });
    }

}
