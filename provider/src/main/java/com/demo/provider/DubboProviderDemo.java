package com.demo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DubboProviderDemo {

    static {
        /*
         * 强制指定Dubbo框架使用SLF4J打印日志，在Dubbo框架的Logger工厂里log4j的优先级比slf4j高，
         * 如果项目中存在log4j的包的话，Dubbo框架的日志会打印不出来，
         * dubbo框架的Logger工厂类：org.apache.dubbo.common.logger.LoggerFactory
         */
        System.setProperty("dubbo.application.logger", "slf4j");
    }

    public static void main(String[] args) {
        System.setProperty("dubbo.application.logger", "slf4j");
        SpringApplication.run(DubboProviderDemo.class, args);
    }

}
