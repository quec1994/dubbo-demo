package com.demo.single;

import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import com.demo.dubbo.RestDemoService;
import org.apache.commons.codec.Charsets;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EnableAutoConfiguration
public class RestDubboConsumerDemo {

    @DubboReference(version = "rest", protocol = "rest")
    private RestDemoService restDemoService;

    public static void main(String[] args) {
        dubboDemo();
        httpDemo();
    }

    private static void dubboDemo() {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(RestDubboConsumerDemo.class);
        RestDemoService restDemoService = context.getBean(RestDemoService.class);
        System.out.println("dubboDemo：" + restDemoService.sayHello("World"));
    }

    private static void httpDemo() {
        String spec = "http://localhost:10880/demo/say?name=World";
        System.out.println("httpDemo：" + MHttpClient.get(spec));
    }

    public static class MHttpClient {
        public static String get(String url) {
            // 创建HttpClient实例
            try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
                // 根据URL创建HttpGet实例
                HttpGet get = new HttpGet(url);
                // 执行get请求，得到返回体
                HttpResponse response = client.execute(get);
                // 判断是否正常返回
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    // 解析数据
                    return EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
                }
                return url;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static String post(String url) {
            // 创建HttpClient实例
            try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
                // 根据URL创建HttpPost实例
                HttpPost post = new HttpPost(url);
                // 构造post参数
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("name", "11"));
                // 编码格式转换
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
                // 传入请求体
                post.setEntity(entity);
                // 发送请求，得到响应体
                HttpResponse response = client.execute(post);
                // 判断是否正常返回
                if (response.getStatusLine().getStatusCode() == 200) {
                    // 解析数据
                    HttpEntity resEntity = response.getEntity();
                    return EntityUtils.toString(resEntity);
                }
                return url;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
