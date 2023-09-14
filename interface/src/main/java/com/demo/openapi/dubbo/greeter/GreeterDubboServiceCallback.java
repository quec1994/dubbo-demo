package com.demo.openapi.dubbo.greeter;

public interface GreeterDubboServiceCallback {

    void changed(String key, String msg);

    void result(String key, String msg);

    boolean available(String key);

}
