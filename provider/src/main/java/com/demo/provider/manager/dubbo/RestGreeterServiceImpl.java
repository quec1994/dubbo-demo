package com.demo.provider.manager.dubbo;

import com.demo.dubbo.RestGreeterService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;


@DubboService(group ="rest", protocol = "rest")
public class RestGreeterServiceImpl implements RestGreeterService {

    @Override
    public String sayHello(String name) {
        System.out.println(name + " 执行了rest服务");

        URL url = RpcContext.getServiceContext().getUrl();
        return String.format("%s：%s, Hello %s", url.getProtocol(), url.getPort(), name);
    }

}
