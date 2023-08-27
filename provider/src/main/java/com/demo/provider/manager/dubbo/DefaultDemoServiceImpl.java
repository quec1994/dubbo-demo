package com.demo.provider.manager.dubbo;

import com.demo.dubbo.DemoService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;


@DubboService(version = "default")
public class DefaultDemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println(name + " 执行了default服务");

        URL url = RpcContext.getServiceContext().getUrl();
        return String.format("%s：%s, Hello %s", url.getProtocol(), url.getPort(), name);  // 正常访问
    }


}
