package com.demo.provider.openapi.dubbo.greeter;

import com.demo.common.assertion.ParameterAssert;
import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import com.demo.openapi.dubbo.greeter.GreeterDubboServiceCallback;
import com.demo.provider.service.greeter.CallBackGreeterService;
import org.apache.dubbo.config.annotation.Argument;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.beans.factory.annotation.Autowired;


// DemoService的sayHello方法的index=1的参数是回调对象，服务消费者可以调用addListener方法来添加回调对象，服务提供者一旦执行回调对象的方法就会通知给服务消费者

@DubboService(group ="callback", methods = {@Method(name = "sayHello", arguments = {@Argument(index = 2, callback = true)})}, callbacks = 3)
public class CallBackGreeterDubboServiceImpl implements GreeterDubboService {

    @Autowired
    private CallBackGreeterService callBackGreeterService;

    @Override
    public String sayHello(String name) {
        return callBackGreeterService.sayHello(name);
    }

    @Override
    public String sayHello(String name, String key, GreeterDubboServiceCallback callback) {
        ParameterAssert.hasText(name, "name");
        System.out.println(name + " 执行了回调服务");
        return callBackGreeterService.sayHello(name, key, callback);
    }

}
