package com.demo.provider.service.greeter;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExceptionGreeterService {

    @Autowired
    private DefaultGreeterService defaultGreeterService;

    public String sayHello(String name) {
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
        return defaultGreeterService.sayHello(name);
    }

    public static class CustomizeException extends RuntimeException {

        public CustomizeException(String message) {
            super(message);
        }

    }

}
