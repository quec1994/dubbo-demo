package com.demo.consumer.single;

import com.demo.openapi.dubbo.greeter.GreeterDubboProtoReply;
import com.demo.openapi.dubbo.greeter.GreeterDubboProtoRequest;
import com.demo.openapi.dubbo.greeter.GreeterDubboProtoService;
import com.google.common.base.Stopwatch;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import com.demo.consumer.single.base.BaseSingleDubboConsumerDemoTest;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ProtoDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {

    @DubboReference(group = "proto", timeout = 4000)
    private GreeterDubboProtoService greeterDubboProtoService;

    @Test
    public void testUnary() {
        GreeterDubboProtoRequest GreeterDubboProtoRequest = com.demo.openapi.dubbo.greeter.GreeterDubboProtoRequest.newBuilder()
                .setName("世界")
                .build();
        final GreeterDubboProtoReply reply = greeterDubboProtoService.greet(GreeterDubboProtoRequest);
        System.out.println("unary：" + reply.getMessage());
    }

    @Test
    public void testGreetStream() {
        Stopwatch stopwatchOuter = Stopwatch.createStarted();
        String methodFlag = "greetStream";
        String methodFlagOuter = methodFlag + ".outer";
        StreamObserver<GreeterDubboProtoReply> responseObserver = new StreamObserver<GreeterDubboProtoReply>() {

            {
                printMessage(stopwatchOuter + " construct", methodFlag + ".init");
            }

            private final Stopwatch stopwatch = Stopwatch.createStarted();

            @Override
            public void onNext(GreeterDubboProtoReply reply) {
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
        StreamObserver<GreeterDubboProtoRequest> requestStreamObserver = greeterDubboProtoService.greetStream(responseObserver);
        sleepAndRunAndPrintForMessage(1, methodFlagOuter, stopwatchOuter,
                () -> requestStreamObserver.onNext(buildGreeterDubboProtoRequest(stopwatchOuter)));
        requestStreamObserver.onCompleted();
        waitCompleted();
        printMessage(stopwatchOuter + " end", methodFlagOuter);
    }

    private GreeterDubboProtoRequest buildGreeterDubboProtoRequest(Stopwatch stopwatch) {
        return GreeterDubboProtoRequest.newBuilder()
                .setName("Client " + stopwatch)
                .build();
    }

    @Test
    public void testGreetServerStream() {
        Stopwatch stopwatchOuter = Stopwatch.createStarted();
        String methodFlag = "greetServerStream";
        String methodFlagOuter = methodFlag + ".outer";
        StreamObserver<GreeterDubboProtoReply> responseObserver = new StreamObserver<GreeterDubboProtoReply>() {
            private final Stopwatch stopwatch = Stopwatch.createStarted();

            @Override
            public void onNext(GreeterDubboProtoReply reply) {
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
        greeterDubboProtoService.greetServerStream(GreeterDubboProtoRequest.newBuilder()
                .setName("Client " + stopwatchOuter)
                .build(), responseObserver);
        waitCompleted();
        printMessage(stopwatchOuter + " end", methodFlagOuter);
    }

    private void sleepAndPrintEndMessage(int timeout, String methodFlag, Stopwatch stopwatch) {
        methodFlag += "-End";
        sleepAndRun(timeout, methodFlag, stopwatch, null);
    }

    private void sleepAndRunAndPrintForMessage(int timeout, String methodFlag, Stopwatch stopwatch, Runnable runnable) {
        IntStream.rangeClosed(1, 10)
                .forEach((i) -> sleepAndRun(timeout, methodFlag + "-For", stopwatch, runnable));
    }

    private void sleepAndRun(int timeout, String methodFlag, Stopwatch stopwatchOuter, Runnable runnable) {
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

    private void printMessage(Throwable throwable, String methodFlag) {
        printMessage("发生了异常", methodFlag);
        throwable.printStackTrace();
    }

    private void printReceivedMessage(String message, String methodFlag, Stopwatch stopwatch) {
        printMessage(stopwatch + " [" + message + "]", methodFlag + "-Received");
    }

    private void printMessage(String message, String methodFlag) {
        String now = DateTimeFormatter.ISO_TIME.format(LocalTime.now());
        System.out.println(now + " [" + Thread.currentThread().getName() + "] " + methodFlag + "：" + message);
    }

    private synchronized void waitCompleted() {
        try {
            ProtoDubboConsumerDemoTest.class.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void notifyCompleted() {
        ProtoDubboConsumerDemoTest.class.notify();
    }

}
