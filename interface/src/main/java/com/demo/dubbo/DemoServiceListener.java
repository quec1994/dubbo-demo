package com.demo.dubbo;

public interface DemoServiceListener {

    void changed(String key, String msg);

    void result(String key, String msg);

    boolean available(String key);

}
