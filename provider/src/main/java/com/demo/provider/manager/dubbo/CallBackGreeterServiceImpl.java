package com.demo.provider.manager.dubbo;

import com.demo.dubbo.GreeterService;
import com.demo.dubbo.GreeterServiceCallback;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.Argument;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.rpc.RpcContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


// DemoService的sayHello方法的index=1的参数是回调对象，服务消费者可以调用addListener方法来添加回调对象，服务提供者一旦执行回调对象的方法就会通知给服务消费者

@DubboService(group ="callback", methods = {@Method(name = "sayHello", arguments = {@Argument(index = 2, callback = true)})}, callbacks = 3)
public class CallBackGreeterServiceImpl implements GreeterService {

    private final Map<String, GreeterServiceCallback> listeners = new ConcurrentHashMap<>();

    public CallBackGreeterServiceImpl() {
        Thread t = new Thread(() -> {
            while (true) {
                listeners.entrySet().forEach(entry -> {
                    try {
                        this.notify(entry);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

    }

    private void notify(Map.Entry<String, GreeterServiceCallback> entry) {
        String key = entry.getKey();
        GreeterServiceCallback callback = entry.getValue();
        boolean available = callback.available(key);
        if (available) {
            callback.changed(key, getChanged());
        } else {
            listeners.remove(key);
            callback.result(key, "监听移除成功");
        }
    }

    private String getChanged() {
        return "Changed: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @Override
    public String sayHello(String name) {
        return null;
    }

    @Override
    public String sayHello(String name, String key, GreeterServiceCallback callback) {
        System.out.println(name + " 执行了回调服务");

        listeners.put(key, callback);
        callback.result(key, "注册成功");
        return String.format("您好 %s！", name);
    }

}
