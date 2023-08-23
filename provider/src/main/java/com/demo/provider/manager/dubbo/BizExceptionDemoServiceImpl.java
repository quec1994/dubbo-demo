package com.demo.provider.manager.dubbo;

import com.demo.dubbo.DemoService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;


@DubboService(version = "bizException")
public class BizExceptionDemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println(name + " 执行了bizException服务");

        URL url = RpcContext.getContext().getUrl();
        String format = String.format("%s：%s, %s, %s", url.getProtocol(), url.getPort(), "%s", name);
        if (name.equals("customize")) {
            throw new CustomizeException(String.format(format, "CustomizeException"));
        }
        if (name.equals("null")) {
            throw new NullPointerException(String.format(format, "NullPointerException"));
        }
        if (name.equals("rpc")) {
            throw new RpcException(String.format(format, "RpcException"));
        }
        if (name.equals("rpc_biz")) {
            throw new RpcException(RpcException.BIZ_EXCEPTION, String.format(format, "RpcException_BIZ"));
        }
        return String.format("%s：%s, Hello %s", url.getProtocol(), url.getPort(), name);
    }

    public static class CustomizeException extends RuntimeException {

        public CustomizeException(String message) {
            super(message);
        }

    }

}
