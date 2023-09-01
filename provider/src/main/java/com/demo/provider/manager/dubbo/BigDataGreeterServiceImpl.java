package com.demo.provider.manager.dubbo;

import com.demo.dubbo.GreeterService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

@DubboService(version = "bigData", protocol = {"tri", "dubbo"})
public class BigDataGreeterServiceImpl implements GreeterService {

    @Override
    public String sayHello(String name) {
        URL url = RpcContext.getServiceContext().getUrl();
        System.out.printf("%s：%s, %s 执行了bigData服务",
                url.getProtocol(), url.getPort(), name);
        System.out.println();

        StringBuilder responseBuilder = new StringBuilder(name);
        for (int i = 0; i < 20; i++) {
            responseBuilder.append(responseBuilder);
        }
        return responseBuilder.toString();
    }

}
