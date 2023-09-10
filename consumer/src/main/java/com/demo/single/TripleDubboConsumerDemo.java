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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@EnableAutoConfiguration
public class TripleDubboConsumerDemo {

    @DubboReference(group = "triple", timeout = 4000)
    private GreeterProtoService greeterProtoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SingleDubboConsumerDemoStarter.run(TripleDubboConsumerDemo.class);
        GreeterProtoService greeterService = context.getBean(GreeterProtoService.class);
//        unary(greeterService);
//        greetStream(greeterService);
        greetServerStream(greeterService);
    }

    private static void unary(GreeterProtoService greeterService) {
        GreeterRequest greeterRequest = GreeterRequest.newBuilder()
                .setName("世界")
                .build();
        final GreeterReply reply = greeterService.greet(greeterRequest);
        System.out.println("unary：" + reply.getMessage());
    }

    private static void greetStream(GreeterProtoService greeterService) {
        Stopwatch stopwatchOuter = Stopwatch.createStarted();
        String methodFlag = "greetStream";
        String methodFlagOuter = methodFlag + ".outer";
        StreamObserver<GreeterReply> responseObserver = new StreamObserver<GreeterReply>() {

            {
                printMessage(stopwatchOuter + " construct", methodFlag + ".init");
            }

            private final Stopwatch stopwatch = Stopwatch.createStarted();

            @Override
            public void onNext(GreeterReply reply) {
                String methodFlagInternal = methodFlag + ".onNext";
                printReceivedMessage(reply.getMessage(), methodFlagInternal, stopwatch);
                sleepAndPrintEndMessage(4, methodFlagInternal, stopwatch);
            }

            @Override
            public void onError(Throwable throwable) {
                printMessage(throwable, methodFlag + ".onError");
            }

            @Override
            public void onCompleted() {
                String methodFlagInternal = methodFlag + ".onCompleted";
                printReceivedMessage("completed", methodFlagInternal, stopwatch);
                sleepAndPrintEndMessage(2, methodFlagInternal, stopwatch);
                notifyCompleted();
            }
        };
        printMessage(stopwatchOuter + " start", methodFlagOuter);
        StreamObserver<GreeterRequest> requestStreamObserver = greeterService.greetStream(responseObserver);
        sleepAndRunAndPrintForMessage(1, methodFlagOuter, stopwatchOuter,
                () -> requestStreamObserver.onNext(buildGreeterRequest(stopwatchOuter)));
        requestStreamObserver.onCompleted();
        waitCompleted();
        printMessage(stopwatchOuter + " end", methodFlagOuter);
    }

    private static GreeterRequest buildGreeterRequest(Stopwatch stopwatch) {
        return GreeterRequest.newBuilder()
                .setName("Client " + stopwatch)
                .build();
    }

    private static void greetServerStream(GreeterProtoService greeterService) {
        Stopwatch stopwatchOuter = Stopwatch.createStarted();
        String methodFlag = "greetServerStream";
        String methodFlagOuter = methodFlag + ".outer";
        StreamObserver<GreeterReply> responseObserver = new StreamObserver<GreeterReply>() {
            private final Stopwatch stopwatch = Stopwatch.createStarted();

            @Override
            public void onNext(GreeterReply reply) {
                String methodFlagInternal = methodFlag + ".onNext";
                printReceivedMessage(reply.getMessage(), methodFlagInternal, stopwatch);
                sleepAndPrintEndMessage(3, methodFlagInternal, stopwatch);
            }

            @Override
            public void onError(Throwable throwable) {
                printMessage(throwable, methodFlag + ".onError");
            }

            @Override
            public void onCompleted() {
                String methodFlagInternal = methodFlag + ".onCompleted";
                printReceivedMessage("completed", methodFlagInternal, stopwatch);
                sleepAndPrintEndMessage(2, methodFlagInternal, stopwatch);
                notifyCompleted();
            }
        };
        printMessage(stopwatchOuter + " start", methodFlagOuter);
        greeterService.greetServerStream(GreeterRequest.newBuilder()
                .setName("Client " + stopwatchOuter)
                .build(), responseObserver);
        waitCompleted();
        printMessage(stopwatchOuter + " end", methodFlagOuter);
    }

    private static void sleepAndPrintEndMessage(int timeout, String methodFlag, Stopwatch stopwatch) {
        methodFlag += "-End";
        sleepAndRun(timeout, methodFlag, stopwatch, null);
    }

    private static void sleepAndRunAndPrintForMessage(int timeout, String methodFlag, Stopwatch stopwatch, Runnable runnable) {
        IntStream.rangeClosed(1, 10)
                .forEach((i) -> sleepAndRun(timeout, methodFlag + "-For", stopwatch, runnable));
    }

    private static void sleepAndRun(int timeout, String methodFlag, Stopwatch stopwatchOuter, Runnable runnable) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            printMessage(e, "sleepAndRun[" + methodFlag + "]");
        }
        if (Objects.isNull(methodFlag)) {
            return;
        }
        StringBuilder message = new StringBuilder();
        if (Objects.nonNull(stopwatchOuter)) {
            message.append(stopwatchOuter).append(" [");
        }
        message.append("sleep_elapsed ").append(stopwatch);
        if (Objects.nonNull(runnable)) {
            stopwatch = Stopwatch.createStarted();
            runnable.run();
            message.append(", runnable_elapsed ").append(stopwatch);
        }
        if (Objects.nonNull(stopwatchOuter)) {
            message.append(']');
        }
        printMessage(message.toString(), methodFlag);
    }

    private static void printMessage(Throwable throwable, String methodFlag) {
        printMessage("发生了异常", methodFlag);
        throwable.printStackTrace();
    }

    private static void printReceivedMessage(String message, String methodFlag, Stopwatch stopwatch) {
        printMessage(stopwatch + " [" + message + "]", methodFlag + "-Received");
    }

    private static void printMessage(String message, String methodFlag) {
        String now = DateTimeFormatter.ISO_TIME.format(LocalTime.now());
        System.out.println(now + " [" + Thread.currentThread().getName() + "] " + methodFlag + "：" + message);
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
