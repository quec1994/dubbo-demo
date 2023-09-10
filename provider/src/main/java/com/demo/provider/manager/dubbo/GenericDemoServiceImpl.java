package com.demo.provider.manager.dubbo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Arrays;

@DubboService(interfaceName = "com.demo.dubbo.GreeterService", group ="generic")
public class GenericDemoServiceImpl implements GenericService {

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        System.out.println(Arrays.toString(args) + " 执行了generic服务");

        URL url = RpcContext.getServiceContext().getUrl();
        return String.format("执行的方法是 %s，参数是 %s（通过 %s:%s 请求）", method, Arrays.toString(args),
                url.getProtocol(), url.getPort());
    }

}
