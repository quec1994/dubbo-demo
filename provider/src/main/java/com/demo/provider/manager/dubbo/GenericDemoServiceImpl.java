package com.demo.provider.manager.dubbo;

import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;

@DubboService(interfaceName = "com.demo.DemoService", version = "generic")
public class GenericDemoServiceImpl implements GenericService {
    @Override
    public Object $invoke(String s, String[] strings, Object[] objects) throws GenericException {
        System.out.println("执行了generic服务");

        return "执行的方法是" + s;
    }
}
