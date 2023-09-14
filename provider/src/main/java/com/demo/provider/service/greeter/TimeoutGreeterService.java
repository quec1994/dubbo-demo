package com.demo.provider.service.greeter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class TimeoutGreeterService {

    @Autowired
    private DefaultGreeterService defaultGreeterService;

    public String sayHello(String name) {
        // 服务执行2秒
        // 服务超时时间为1秒，但是执行了2秒，服务端会把任务执行完的
        // 服务的超时时间，是指如果服务执行时间超过了指定的超时时间则会打印一个warning日志
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " 执行结束");
        return defaultGreeterService.sayHello(name);
    }

}
