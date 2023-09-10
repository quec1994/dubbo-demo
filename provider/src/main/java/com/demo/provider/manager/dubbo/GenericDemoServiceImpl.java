package com.demo.provider.manager.dubbo;

import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@DubboService(interfaceName = "com.demo.dubbo.GreeterService", group ="generic")
public class GenericDemoServiceImpl implements GenericService {

    @Autowired
    private GreeterService greeterService;

    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        String name = String.valueOf(args[0]);
        System.out.printf("%s 执行了generic服务，执行的方法是 %s，参数是 %s\n",
                name, method, Arrays.toString(args));
        return greeterService.sayHello(name);
    }

}
