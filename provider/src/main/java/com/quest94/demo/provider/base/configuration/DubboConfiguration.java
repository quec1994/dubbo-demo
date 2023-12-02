package com.quest94.demo.provider.base.configuration;

import com.quest94.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.ProviderConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.rpc.protocol.dubbo.DubboProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableDubbo(scanBasePackages = "com.demo.provider.openapi.dubbo")
public class DubboConfiguration {

    private static final String PROTOCOL_ID_DUBBO = CommonConstants.DUBBO;
    private static final String PROTOCOL_ID_REST = "rest";
    private static final String PROTOCOL_ID_TRIPLE = CommonConstants.TRIPLE;
    private static final String REGISTRY_ID_PROVIDER_NACOS = "providerNacosRegistry";

    static {
        forceAssignLoggerFactory();
    }

    @DubboReference(group ="default")
    private GreeterDubboService greeterDubboService;

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

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * @return 应用配置
     */
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

    /**
     * @return 注册中心配置
     */
    @Bean(REGISTRY_ID_PROVIDER_NACOS)
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setId(REGISTRY_ID_PROVIDER_NACOS);
        registryConfig.setAddress("nacos://127.0.0.1:8848");
        return registryConfig;
    }

    /**
     * @return Dubbo 协议配置
     */
    @Bean(PROTOCOL_ID_DUBBO)
    public ProtocolConfig dubboProtocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setId(PROTOCOL_ID_DUBBO);
        protocolConfig.setName(CommonConstants.DUBBO);
        protocolConfig.setPort(DubboProtocol.DEFAULT_PORT);
        return protocolConfig;
    }

    /**
     * @return Rest 协议配置
     */
    @Bean(PROTOCOL_ID_REST)
    public ProtocolConfig restProtocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setId(PROTOCOL_ID_REST);
        protocolConfig.setName("rest");
        protocolConfig.setPort(80);
        protocolConfig.setDefault(Boolean.FALSE);
        return protocolConfig;
    }

    /**
     * @return Triple 协议配置
     */
    @Bean(PROTOCOL_ID_TRIPLE)
    public ProtocolConfig tripleProtocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setId(PROTOCOL_ID_TRIPLE);
        protocolConfig.setName(CommonConstants.TRIPLE);
        protocolConfig.setPort(50051);
        protocolConfig.setDefault(Boolean.FALSE);
        return protocolConfig;
    }

    /**
     * @return 提供者公共/默认配置
     */
    @Bean
    public ProviderConfig providerConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setRegistryIds(REGISTRY_ID_PROVIDER_NACOS);
        return providerConfig;
    }

}
