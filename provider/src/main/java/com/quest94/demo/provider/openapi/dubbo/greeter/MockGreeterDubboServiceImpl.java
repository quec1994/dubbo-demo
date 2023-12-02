package com.quest94.demo.provider.openapi.dubbo.greeter;

import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboService;
import com.quest94.demo.provider.common.assertion.ParameterAssert;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcException;


@DubboService(group ="mock", timeout = 1000, cluster = "failsafe"
        // 使用com.demo.dubbo.DemoServiceMock实现类的方法返回模拟数据
        , mock = "true"
)
public class MockGreeterDubboServiceImpl implements GreeterDubboService {

    @Override
    public String sayHello(String name) {
        ParameterAssert.hasText(name, "name");
        System.out.println(name + " 执行了mock服务");
        throw new RpcException("接口还未准备好，先用mock数据");
    }

}
