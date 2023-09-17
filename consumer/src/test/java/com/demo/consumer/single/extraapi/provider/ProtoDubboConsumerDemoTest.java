package com.demo.consumer.single.extraapi.provider;

import com.demo.consumer.single.extraapi.provider.base.BaseProviderDubboConsumerDemoSingleTest;
import com.demo.openapi.dubbo.greeter.GreeterProtoDubboService;
import com.demo.openapi.dubbo.greeter.GreeterProtoReply;
import com.demo.openapi.dubbo.greeter.GreeterProtoRequest;
import com.google.common.base.Stopwatch;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class ProtoDubboConsumerDemoTest extends BaseProviderDubboConsumerDemoSingleTest {

    private static final ReentrantLock LOCK = new ReentrantLock();

    @DubboReference(group = "proto", timeout = 4000)
    private GreeterProtoDubboService greeterProtoDubboService;

    @Test
    public void testUnary() {
        final GreeterProtoReply reply = greeterProtoDubboService.greet(GreeterProtoRequest.newBuilder()
                .setName("世界")
                .build());
        System.out.println("unary：" + reply.getMessage());
    }

    @Test
    public void testGreetStream() {
        Condition condition = LOCK.newCondition();
        Stopwatch stopwatchOuter = Stopwatch.createStarted();
        String methodFlag = "greetStream";
        String methodFlagOuter = methodFlag + ".outer";
        StreamObserver<GreeterProtoReply> responseObserver = new StreamObserver<GreeterProtoReply>() {

            {
                printMessage(stopwatchOuter + " construct", methodFlag + ".init");
            }

            private final Stopwatch stopwatch = Stopwatch.createStarted();

            @Override
            public void onNext(GreeterProtoReply reply) {
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
                notifyCompleted(condition);
            }
        };
        printMessage(stopwatchOuter + " start", methodFlagOuter);
        StreamObserver<GreeterProtoRequest> requestStreamObserver = greeterProtoDubboService.greetStream(responseObserver);
        sleepAndRunAndPrintForMessage(1, methodFlagOuter, stopwatchOuter,
                () -> requestStreamObserver.onNext(buildGreeterProtoRequest(stopwatchOuter)));
        requestStreamObserver.onCompleted();
        waitCompleted(condition);
        printMessage(stopwatchOuter + " end", methodFlagOuter);
    }

    private GreeterProtoRequest buildGreeterProtoRequest(Stopwatch stopwatch) {
        return GreeterProtoRequest.newBuilder()
                .setName("Client " + stopwatch)
                .build();
    }

    @Test
    public void testGreetServerStream() {
        Condition condition = LOCK.newCondition();
        Stopwatch stopwatchOuter = Stopwatch.createStarted();
        String methodFlag = "greetServerStream";
        String methodFlagOuter = methodFlag + ".outer";
        StreamObserver<GreeterProtoReply> responseObserver = new StreamObserver<GreeterProtoReply>() {
            private final Stopwatch stopwatch = Stopwatch.createStarted();

            @Override
            public void onNext(GreeterProtoReply reply) {
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
                notifyCompleted(condition);
            }
        };
        printMessage(stopwatchOuter + " start", methodFlagOuter);
        greeterProtoDubboService.greetServerStream(GreeterProtoRequest.newBuilder()
                .setName("Client " + stopwatchOuter)
                .build(), responseObserver);
        waitCompleted(condition);
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

    private void waitCompleted(Condition condition) {
        LOCK.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    private void notifyCompleted(Condition condition) {
        LOCK.lock();
        try {
            condition.signalAll();
        } finally {
            LOCK.unlock();
        }
    }

}
