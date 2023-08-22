package com.demo.provider.manager.dubbo;

import com.demo.dubbo.DemoService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;


@DubboService(version = "rest", protocol = "rest")
@Path("demo")
public class RestDemoServiceImpl implements DemoService {

    @GET
    @Path("say")
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    @Override
    public String sayHello(@QueryParam("name") String name) {
        System.out.println(name + " 执行了rest服务");

        URL url = RpcContext.getContext().getUrl();
        return String.format("%s: %s, Hello, %s", url.getProtocol(), url.getPort(), name);  // 正常访问
    }

}
