package com.quest94.demo.openapi.dubbo.greeter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("demo")
public interface GreeterDubboRestService {

    @GET
    @Path("say")
    String sayHello(@QueryParam("name") String name);

}
