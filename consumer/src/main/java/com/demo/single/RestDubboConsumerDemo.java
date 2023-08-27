package com.demo.single;

import com.demo.dubbo.RestDemoService;
import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@EnableAutoConfiguration
public class RestDubboConsumerDemo {

    @DubboReference(version = "rest")
    private RestDemoService restDemoService;

    public static void main(String[] args) {
        dubboDemo();
//        httpDemo();
    }

    private static void dubboDemo() {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(RestDubboConsumerDemo.class);
        RestDemoService restDemoService = context.getBean(RestDemoService.class);
        System.out.println("dubboDemo：" + restDemoService.sayHello("World"));
    }

    private static void httpDemo() {
        String spec = "http://localhost:10880/demo/say?name=World";
        System.out.println("httpDemo：" + HttpClient.get(spec));
    }

    public static class HttpClient {

        public static String get(String url) {
            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .build();
            final Call call = okHttpClient.newCall(request);
            try (Response response = call.execute()) {
                if (response.body() != null) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
