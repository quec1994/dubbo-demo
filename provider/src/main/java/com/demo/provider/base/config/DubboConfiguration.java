package com.demo.provider.base.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo(scanBasePackages = "com.demo.provider.manager.dubbo")
@PropertySource(value = "classpath:/dubbo/dubbo-provider.yml", factory = YamlPropertySourceFactory.class)
public class DubboConfiguration {

    static {
        forceAssignLoggerFactory();
    }

    private static void forceAssignLoggerFactory() {
        /*
         * 强制指定Dubbo框架使用SLF4J打印日志，在Dubbo框架的Logger工厂里log4j的优先级比slf4j高，
         * 如果项目中存在log4j的包的话，Dubbo框架的日志会打印不出来，
         * dubbo框架的Logger工厂类：org.apache.dubbo.common.logger.LoggerFactory
         */
        String loggerKey = "dubbo.application.logger";
        String logger = System.getProperty(loggerKey);
        if (StringUtils.isBlank(logger)) {
            System.setProperty(loggerKey, "slf4j");
        }
    }

}
