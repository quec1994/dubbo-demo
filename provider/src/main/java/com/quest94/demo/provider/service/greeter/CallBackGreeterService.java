package com.quest94.demo.provider.service.greeter;

import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboServiceCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CallBackGreeterService {

    @Autowired
    private DefaultGreeterService defaultGreeterService;

    public String sayHello(String name) {
        return defaultGreeterService.sayHello(name);
    }

    public String sayHello(String name, String key, GreeterDubboServiceCallback callback) {
        listeners.put(key, callback);
        callback.result(key, "注册成功");
        return defaultGreeterService.sayHello(name);
    }

    private final Map<String, GreeterDubboServiceCallback> listeners = new ConcurrentHashMap<>();

    public CallBackGreeterService() {
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

    private void notify(Map.Entry<String, GreeterDubboServiceCallback> entry) {
        String key = entry.getKey();
        GreeterDubboServiceCallback callback = entry.getValue();
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

}
