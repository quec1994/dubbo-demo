package com.demo.consumer.extraapi.provider;

import com.demo.openapi.dubbo.greeter.GreeterDubboService;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderDubbo {

    private static final String NACOS_REGISTRY = "consumerRegistryProviderNacos";

    @Bean(NACOS_REGISTRY)
    public RegistryConfig providerRegistry() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setId(NACOS_REGISTRY);
        registryConfig.setAddress("nacos://127.0.0.1:8848");
        return registryConfig;
    }

    @DubboReference(id = "defaultGreeterDubboService", group = "default", registry = NACOS_REGISTRY)
    private GreeterDubboService defaultGreeterDubboService;

}
