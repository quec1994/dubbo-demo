package com.demo.provider.manager.dubbo;

import com.demo.dubbo.GreeterService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;


@DubboService(version = "exception")
public class ExceptionGreeterServiceImpl implements GreeterService {

    @Override
    public String sayHello(String name) {
        System.out.println(name + " 执行了exception服务");

        URL url = RpcContext.getServiceContext().getUrl();
        String format = String.format("%s：%s, %s", url.getProtocol(), url.getPort(), "%s");
        if (name.equals("customize")) {
            // 不会触发消费者端集群容错，也不会消费者端触发mock容错
            throw new CustomizeException(String.format(format, "CustomizeException"));
        }
        if (name.equals("null")) {
            // 不会触发消费者端集群容错，也不会触发消费者端mock容错
            throw new NullPointerException(String.format(format, "NullPointerException"));
        }
        if (name.equals("rpc")) {
            // 不会触发消费者端集群容错，但是可以触发消费者端mock容错
            throw new RpcException(String.format(format, "RpcException"));
        }
        if (name.equals("rpc_biz")) {
            // 不会触发消费者端集群容错，也不会消费者端触发mock容错
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
