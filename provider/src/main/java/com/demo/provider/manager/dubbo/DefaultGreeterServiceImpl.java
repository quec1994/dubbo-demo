package com.demo.provider.manager.dubbo;

import com.demo.dubbo.GreeterService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;


@DubboService(group ="default")
public class DefaultGreeterServiceImpl implements GreeterService {

    @Override
    public String sayHello(String name) {
        System.out.println(name + " 执行了default服务");

        URL url = RpcContext.getServiceContext().getUrl();
        return String.format("Hello %s（通过 %s:%s 请求）", name, url.getProtocol(), url.getPort());
    }


}
