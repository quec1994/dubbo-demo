package com.demo.consumer.base.configuration;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableDubbo
public class DubboConfiguration {

    static {
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
        /*
         * 强制指定Dubbo应用的qosEnable值为false，
         * dubbo框架在spring环境变量中添加的默认值比@Bean配置的优先级高，
         * dubbo框架默认值构造类：org.apache.dubbo.spring.boot.env.DubboDefaultPropertiesEnvironmentPostProcessor
         */
        String qosEnableKey = "dubbo.application.qos-enable";
        String qosEnable = System.getProperty(qosEnableKey);
        if (StringUtils.isBlank(qosEnable)) {
            System.setProperty(qosEnableKey, Boolean.FALSE.toString());
        }
    }

    @Value("${spring.application.name}")
    private String applicationName;

    // 应用配置
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(applicationName);
        /*
         * 可选值
         * 控制provider服务导出时服务注册行为，应用级服务发现迁移用
         * - instance 只注册应用级服务；
         * - interface 只注册接口级服务；
         * - all 同时注册应用级和接口级服务，默认就是这个
         */
        applicationConfig.setRegisterMode("instance");
        /*
         * 可选值
         * Dubbo QOS
         */
        applicationConfig.setQosEnable(Boolean.FALSE);
        Map<String, String> parameters = new HashMap<>();
        /*
         * 可选值
         * 控制consumer服务引入时服务发现行为，应用级服务发现迁移用
         * - FORCE_INTERFACE 强制只使用接口级服务发现
         * - FORCE_APPLICATION 强制只使用应用级服务发现
         * - APPLICATION_FIRST 智能选择是接口级还是应用级，默认就是这个
         */
        parameters.put("migration.step", "FORCE_APPLICATION");
        applicationConfig.setParameters(parameters);
        return applicationConfig;
    }

}
