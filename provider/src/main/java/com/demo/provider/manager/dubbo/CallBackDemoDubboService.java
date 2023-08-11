package com.demo.provider.manager.dubbo;

import com.demo.DemoService;
import com.demo.DemoServiceListener;
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

@DubboService(version = "callback", methods = {@Method(name = "sayHello", arguments = {@Argument(index = 2, callback = true)})}, callbacks = 3)
public class CallBackDemoDubboService implements DemoService {

    private final Map<String, DemoServiceListener> listeners = new ConcurrentHashMap<>();

    public CallBackDemoDubboService() {
        Thread t = new Thread(() -> {
            while (true) {
                for (Map.Entry<String, DemoServiceListener> entry : listeners.entrySet()) {
                    String key = entry.getKey();
                    DemoServiceListener callback = entry.getValue();
                    boolean available = callback.available(key);
                    if (available) {
                        callback.changed(key, getChanged());
                    } else {
                        listeners.remove(key);
                        callback.result(key, "监听移除成功");
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();

    }

    private String getChanged() {
        return "Changed: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @Override
    public String sayHello(String name) {
        return null;
    }

    @Override
    public String sayHello(String name, String key, DemoServiceListener callback) {
        System.out.println("执行了回调服务" + name);

        listeners.put(key, callback);
        callback.result(key, "注册成功");
        URL url = RpcContext.getContext().getUrl();
        return String.format("%s：%s, Hello, %s", url.getProtocol(), url.getPort(), name);  // 正常访问
    }

}
