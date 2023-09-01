package com.demo.dubbo;

public interface GreeterServiceCallback {

    void changed(String key, String msg);

    void result(String key, String msg);

    boolean available(String key);

}
