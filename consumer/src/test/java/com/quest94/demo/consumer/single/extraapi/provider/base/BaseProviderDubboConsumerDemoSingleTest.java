package com.quest94.demo.consumer.single.extraapi.provider.base;

import com.quest94.demo.consumer.single.base.BaseDubboConsumerDemoSingleTest;
import org.springframework.context.annotation.Import;

@Import(ProviderDubboServiceConfiguration.class)
public abstract class BaseProviderDubboConsumerDemoSingleTest extends BaseDubboConsumerDemoSingleTest {
}

