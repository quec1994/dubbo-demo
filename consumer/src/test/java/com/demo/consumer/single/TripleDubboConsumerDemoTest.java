package com.demo.consumer.single;

import com.demo.proto.dubbo.tri.GreeterProtoService;
import com.demo.proto.dubbo.tri.GreeterReply;
import com.demo.proto.dubbo.tri.GreeterRequest;
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

public class TripleDubboConsumerDemoTest extends BaseSingleDubboConsumerDemoTest {

    @DubboReference(group = "triple", timeout = 4000)
    private GreeterProtoService greeterProtoService;

    @Test
    public void testUnary() {
        GreeterRequest greeterRequest = GreeterRequest.newBuilder()
                .setName("世界")
                .build();
        final GreeterReply reply = greeterProtoService.greet(greeterRequest);
        System.out.println("unary：" + reply.getMessage());
    }

    @Test
    public void testGreetStream() {
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
        StreamObserver<GreeterRequest> requestStreamObserver = greeterProtoService.greetStream(responseObserver);
        sleepAndRunAndPrintForMessage(1, methodFlagOuter, stopwatchOuter,
                () -> requestStreamObserver.onNext(buildGreeterRequest(stopwatchOuter)));
        requestStreamObserver.onCompleted();
        waitCompleted();
        printMessage(stopwatchOuter + " end", methodFlagOuter);
    }

    private GreeterRequest buildGreeterRequest(Stopwatch stopwatch) {
        return GreeterRequest.newBuilder()
                .setName("Client " + stopwatch)
                .build();
    }

    @Test
    public void testGreetServerStream() {
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
        greeterProtoService.greetServerStream(GreeterRequest.newBuilder()
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
            TripleDubboConsumerDemoTest.class.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void notifyCompleted() {
        TripleDubboConsumerDemoTest.class.notify();
    }

}
