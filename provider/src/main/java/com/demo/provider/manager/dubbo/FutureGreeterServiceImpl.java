package com.demo.provider.manager.dubbo;

import com.demo.dubbo.GreeterService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.AsyncContext;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@DubboService(group ="future")
public class FutureGreeterServiceImpl implements GreeterService {

    @Override
    public String sayHello(String name) {
        System.out.println(name + " 执行了同步服务");
        return createResult(name);
    }

    private String createResult(String name) {
        URL url = RpcContext.getServiceContext().getUrl();
        return String.format("您好 %s！", name);
    }

    @Override
    public CompletableFuture<String> sayHelloFuture(String name) {
        System.out.println(name + " 执行了异步方法返回值服务");
        final AsyncContext asyncContext = RpcContext.startAsync();
        return CompletableFuture.supplyAsync(() -> {
            // 如果要使用上下文，则必须要放在第一句执行
            asyncContext.signalContextSwitch();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return this.createResult(name);
        });
    }

}
