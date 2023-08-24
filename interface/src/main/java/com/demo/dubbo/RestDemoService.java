package com.demo.dubbo;

import com.demo.util.ContentType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("demo")
public interface RestDemoService {

    @GET
    @Path("say")
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    String sayHello(@QueryParam("name") String name);

}
