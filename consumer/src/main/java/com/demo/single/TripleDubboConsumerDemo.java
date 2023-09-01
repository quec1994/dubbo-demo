package com.demo.single;

import com.demo.dubbo.tri.grpc.GreeterProtoService;
import com.demo.dubbo.tri.grpc.GreeterReply;
import com.demo.dubbo.tri.grpc.GreeterRequest;
import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class TripleDubboConsumerDemo {

    @DubboReference(version = "triple")
    private GreeterProtoService greeterProtoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(TripleDubboConsumerDemo.class);
        GreeterProtoService greeterService = context.getBean(GreeterProtoService.class);
        GreeterRequest greeterRequest = GreeterRequest.newBuilder()
                .setName("World")
                .build();
        unary(greeterService, greeterRequest);
//        greetStream(greeterService, greeterRequest);
//        greetServerStream(greeterService, greeterRequest);
    }

    private static void unary(GreeterProtoService greeterService, GreeterRequest greeterRequest) {
        final GreeterReply reply = greeterService.greet(greeterRequest);
        System.out.println("unary：" + reply.getMessage());
    }

    private static void greetStream(GreeterProtoService greeterService, GreeterRequest greeterRequest) {
        StreamObserver<GreeterRequest> greeterRequestStreamObserver = greeterService.greetStream(new StreamObserver<GreeterReply>() {
            @Override
            public void onNext(GreeterReply reply) {
                System.out.println("greetStream：" + reply.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("greetStream：onCompleted");
            }
        });
        for (int i = 0; i < 10; i++) {
            GreeterRequest.Builder builder = GreeterRequest.newBuilder(greeterRequest);
            builder.setName(builder.getName() + "-" + i);
            greeterRequestStreamObserver.onNext(builder.build());
        }
        greeterRequestStreamObserver.onCompleted();
    }

    private static void greetServerStream(GreeterProtoService greeterService, GreeterRequest greeterRequest) {
        greeterService.greetServerStream(greeterRequest, new StreamObserver<GreeterReply>() {
            @Override
            public void onNext(GreeterReply reply) {
                System.out.println("greetServerStream：" + reply.getMessage());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("greetServerStream：onCompleted");
            }
        });
    }

}
