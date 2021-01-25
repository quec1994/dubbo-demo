package com.tuling.provider.service;

import com.tuling.DemoService;
import com.tuling.DemoServiceListener;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


//@Service
//@Path("demo")
public class RestDemoService implements DemoService {

    private final Map<String, DemoServiceListener> listeners = new ConcurrentHashMap<String, DemoServiceListener>();

    @GET
    @Path("say")
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    @Override
    public String sayHello(@QueryParam("name") String name) {

        System.out.println("执行了");
//        URL url = RpcContext.getContext().getUrl();
//        return String.format("%s: %s, Hello, %s", url.getProtocol(), url.getPort(), name);  // 正常访问

        return "xxx";

    }

}
