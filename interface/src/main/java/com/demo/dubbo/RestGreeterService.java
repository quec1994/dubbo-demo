package com.demo.dubbo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("demo")
public interface RestGreeterService {

    @GET
    @Path("say")
    String sayHello(@QueryParam("name") String name);

}
