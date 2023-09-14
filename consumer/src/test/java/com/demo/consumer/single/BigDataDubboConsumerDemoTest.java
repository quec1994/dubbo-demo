package com.demo.consumer.single;

import com.demo.consumer.single.base.BaseSingleDubboConsumerDemoTest;
import com.demo.dubbo.GreeterService;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;

public class BigDataDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {

    //    @DubboReference(group ="bigData", protocol = CommonConstants.DUBBO)
    @DubboReference(group ="bigData", protocol = CommonConstants.TRIPLE)
    private GreeterService greeterService;

    @Test
    public void test() {
        long st = System.currentTimeMillis();
        String reply = greeterService.sayHello("World");
        // 5MB response
        System.out.println("Reply length:" + reply.length() + " cost:" + (System.currentTimeMillis() - st));
    }

}
