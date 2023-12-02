package com.quest94.demo.consumer.single.base;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        // 默认不加载web环境
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        // 指定测试启动类为一个空的bean，这样就不会启动spring容器了
        classes = BaseSingleTest.ApplicationConfiguration.class)
// 指定测试环境的配置文件
@ActiveProfiles("developer")
abstract class BaseSingleTest {

    @EnableAutoConfiguration
    static class ApplicationConfiguration {

    }

}
