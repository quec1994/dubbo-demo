package com.demo.consumer.single.base;

import com.demo.consumer.DubboConsumerDemo;
import com.demo.consumer.base.configuration.DubboConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        // 默认不加载web环境
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                BaseSingleDubboConsumerDemoTest.ApplicationConfiguration.class,
                DubboConfiguration.class
        })
// 指定测试环境的配置文件
@ActiveProfiles("developer")
public class BaseSingleDubboConsumerDemoTest {

    static class ApplicationConfiguration extends DubboConsumerDemo {

    }

}
