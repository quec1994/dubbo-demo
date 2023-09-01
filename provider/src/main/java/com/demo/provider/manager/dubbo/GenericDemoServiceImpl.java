package com.demo.provider.manager.dubbo;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.Arrays;

@DubboService(interfaceName = "com.demo.dubbo.GreeterService", version = "generic")
public class GenericDemoServiceImpl implements GenericService {

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        System.out.println(Arrays.toString(args) + " 执行了generic服务");

        return "执行的方法是" + method;
    }

}
