package com.demo.consumer.single.base;

import com.demo.consumer.base.configuration.DubboConfiguration;
import org.springframework.context.annotation.Import;

@Import(/* 加载dubbo配置 */ DubboConfiguration.class)
public abstract class BaseDubboConsumerDemoSingleTest extends BaseSingleTest {

}
