package com.demo.single;

import com.demo.dubbo.tri.grpc.GreeterProtoService;
import com.demo.dubbo.tri.grpc.GreeterReply;
import com.demo.dubbo.tri.grpc.GreeterRequest;
import com.demo.single.starter.SingleDubboConsumerDemoStarter;
import com.google.common.base.Stopwatch;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@EnableAutoConfiguration
public class TripleDubboConsumerDemo {

    @DubboReference(group = "triple")
    private GreeterProtoService greeterProtoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(TripleDubboConsumerDemo.class);
        GreeterProtoService greeterService = context.getBean(GreeterProtoService.class);
        GreeterRequest greeterRequest = GreeterRequest.newBuilder()
                .setName("World")
                .build();
//        unary(greeterService, greeterRequest);
        greetStream(greeterService, greeterRequest);
//        greetServerStream(greeterService, greeterRequest);
    }

    private static void unary(GreeterProtoService greeterService, GreeterRequest greeterRequest) {
        final GreeterReply reply = greeterService.greet(greeterRequest);
        System.out.println("unary：" + reply.getMessage());
    }

    private static void greetStream(GreeterProtoService greeterService, GreeterRequest greeterRequest) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        StreamObserver<GreeterReply> responseObserver = new StreamObserver<GreeterReply>() {
            Stopwatch stopwatch = Stopwatch.createStarted();

            @Override
            public void onNext(GreeterReply reply) {
                printMessage(reply.getMessage(), "greetStream.onNext", stopwatch);
                sleep(4);
            }

            @Override
            public void onError(Throwable throwable) {
                printMessage(throwable, "greetStream.onError", stopwatch);
            }

            @Override
            public void onCompleted() {
                printMessage("completed", "greetStream.onCompleted", stopwatch);
                sleep(2);
                notifyCompleted();
            }
        };
        StreamObserver<GreeterRequest> requestStreamObserver = greeterService.greetStream(responseObserver);
        for (int i = 0; i < 10; i++) {
            Stopwatch stopwatchT = Stopwatch.createStarted();
            sleep(1);
            String sleepDiff = stopwatchT.toString();
            stopwatchT.reset();
            requestStreamObserver.onNext(newGreeterRequest(greeterRequest, stopwatch, i));
            String onNextDiff = stopwatchT.toString();
            printMessage("sleep diff:" + sleepDiff + " onNext diff:" + onNextDiff, "greetServerStream", stopwatch);
        }
        requestStreamObserver.onCompleted();
        waitCompleted();
        printMessage("end", "greetStream", stopwatch);
    }

    private static GreeterRequest newGreeterRequest(GreeterRequest greeterRequest, Stopwatch stopwatch, int i) {
        GreeterRequest.Builder builder = GreeterRequest.newBuilder(greeterRequest);
        builder.setName(stopwatch + " " + builder.getName() + "-" + i);
        return builder.build();
    }

    private static void greetServerStream(GreeterProtoService greeterService, GreeterRequest greeterRequest) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        StreamObserver<GreeterReply> responseObserver = new StreamObserver<GreeterReply>() {
            private final Stopwatch stopwatch = Stopwatch.createStarted();

            @Override
            public void onNext(GreeterReply reply) {
                printMessage(reply.getMessage(), "greetServerStream.onNext", stopwatch);
                sleep(3);
            }

            @Override
            public void onError(Throwable throwable) {
                printMessage(throwable, "greetServerStream.onError", stopwatch);
            }

            @Override
            public void onCompleted() {
                printMessage("completed", "greetServerStream.onCompleted", stopwatch);
                sleep(2);
                notifyCompleted();
            }
        };
        greeterService.greetServerStream(greeterRequest, responseObserver);
        waitCompleted();
        printMessage("end", "greetServerStream", stopwatch);
    }

    private static void printMessage(Throwable throwable, String methodName, Stopwatch stopwatch) {
        printMessage("响应了异常", methodName, stopwatch);
        throwable.printStackTrace();
    }

    private static void printMessage(String message, String methodName, Stopwatch stopwatch) {
        System.out.println(Thread.currentThread().getName() + " " + stopwatch + " " + methodName + "：" + message);
    }

    private static void sleep(int timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void waitCompleted() {
        try {
            TripleDubboConsumerDemo.class.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void notifyCompleted() {
        TripleDubboConsumerDemo.class.notify();
    }

}
