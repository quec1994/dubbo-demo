package com.demo.provider.manager.dubbo;

import com.demo.dubbo.GreeterService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcException;


@DubboService(group ="mock", timeout = 1000, cluster = "failsafe"
        // 使用com.demo.dubbo.DemoServiceMock实现类的方法返回模拟数据
        , mock = "true"
)
public class MockGreeterServiceImpl implements GreeterService {

    @Override
    public String sayHello(String name) {
        System.out.println(name + " 执行了mock服务");
        throw new RpcException("接口还未准备好，先用mock数据");
    }


}
