package com.demo.provider.openapi.dubbo.greeter;

import com.demo.common.assertion.ParameterAssert;
import com.demo.provider.service.greeter.DefaultGreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@DubboService(interfaceName = "com.demo.openapi.dubbo.GreeterDubboService", group = "generic")
public class GenericDemoServiceImpl implements GenericService {

    @Autowired
    private DefaultGreeterService defaultGreeterService;

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        String name = String.valueOf(args[0]);
        ParameterAssert.hasText(name, "name");
        System.out.printf("%s 执行了generic服务，执行的方法是 %s，参数是 %s\n",
                name, method, Arrays.toString(args));
        return defaultGreeterService.sayHello(name);
    }

}
