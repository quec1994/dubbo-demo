package com.demo.consumer.single.extraapi.provider.base;

import org.apache.dubbo.config.RegistryConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderDubboServiceConfiguration extends com.demo.consumer.extraapi.provider.ProviderDubboServiceConfiguration {

    @Override
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = super.registryConfig();
        registryConfig.setDefault(true);
        return registryConfig;
    }

}
