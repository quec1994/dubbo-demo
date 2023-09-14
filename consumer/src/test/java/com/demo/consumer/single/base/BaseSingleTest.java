package com.demo.consumer.single.base;

import com.demo.consumer.DubboConsumerDemo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        // 默认不加载web环境
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        // 指定测试启动类为一个空路径下的类，这样就不会浪费时间进行扫描了
        classes = BaseSingleTest.ApplicationConfiguration.class)
// 指定测试环境的配置文件
@ActiveProfiles("developer")
class BaseSingleTest {

    static class ApplicationConfiguration extends DubboConsumerDemo {

    }

}
