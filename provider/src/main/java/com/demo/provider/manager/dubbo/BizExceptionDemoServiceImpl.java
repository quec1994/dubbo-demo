package com.demo.provider.manager.dubbo;

import com.demo.dubbo.DemoService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;


@DubboService(version = "bizException")
public class BizExceptionDemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println(name + " 执行了bizException服务");

        URL url = RpcContext.getContext().getUrl();
        if (name.equals("customize")) {
            throw new CustomizeException(String.format("%s：%s, CustomizeException, %s", url.getProtocol(), url.getPort(), name));
        }
        if (name.equals("null")) {
            throw new NullPointerException(String.format("%s：%s, NullPointerException, %s", url.getProtocol(), url.getPort(), name));
        }
        return String.format("%s：%s, Hello %s", url.getProtocol(), url.getPort(), name);
    }

    public static class CustomizeException extends RuntimeException {

        public CustomizeException(String message) {
            super(message);
        }

    }

}
