package com.quest94.demo.consumer.single.extraapi.provider;

import com.quest94.demo.consumer.single.extraapi.provider.base.BaseProviderDubboConsumerDemoSingleTest;
import com.quest94.demo.openapi.dubbo.greeter.GreeterProtoDubboService;
import com.quest94.demo.openapi.dubbo.greeter.GreeterProtoReply;
import com.quest94.demo.openapi.dubbo.greeter.GreeterProtoRequest;
import com.google.common.base.Stopwatch;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

public class ProtoDubboConsumerDemoTest extends BaseProviderDubboConsumerDemoSingleTest {

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
        String methodFlag = "greetStream";
        String methodFlagOuter = methodFlag + ".outer";
        testGreetServerStream(methodFlag,
                (stopwatchOuter, responseObserver) -> {
                    StreamObserver<GreeterProtoRequest> requestStreamObserver = greeterProtoDubboService.greetStream(responseObserver);
                    sleepAndRunAndPrintForMessage(1, methodFlagOuter, stopwatchOuter,
                            () -> requestStreamObserver.onNext(buildGreeterProtoRequest(stopwatchOuter)));
                    requestStreamObserver.onCompleted();
                });
    }

    private void testGreetServerStream(String methodFlag, BiConsumer<Stopwatch, ResponseObserver> biConsumer) {
        Stopwatch stopwatchOuter = Stopwatch.createStarted();
        String methodFlagOuter = methodFlag + ".outer";
        ResponseObserver responseObserver = new ResponseObserver(methodFlag, stopwatchOuter);
        printMessage(stopwatchOuter + " start", methodFlagOuter);
        biConsumer.accept(stopwatchOuter, responseObserver);
        String msg = responseObserver.get(Integer.MAX_VALUE, TimeUnit.MINUTES);
        printMessage(stopwatchOuter + " " + msg + " end", methodFlagOuter);
    }

    private GreeterProtoRequest buildGreeterProtoRequest(Stopwatch stopwatch) {
        return GreeterProtoRequest.newBuilder()
                .setName("Client " + stopwatch)
                .build();
    }

    @Test
    public void testGreetServerStream() {
        String methodFlag = "greetServerStream";
        testGreetServerStream(methodFlag,
                (stopwatchOuter, responseObserver) ->
                        greeterProtoDubboService.greetServerStream(buildGreeterProtoRequest(stopwatchOuter), responseObserver));
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

    private class ResponseObserver extends CompletableFuture<String> implements StreamObserver<GreeterProtoReply> {

        private final String methodFlag;
        private final Stopwatch stopwatch = Stopwatch.createStarted();

        public ResponseObserver(String methodFlag, Stopwatch stopwatchOuter) {
            this.methodFlag = methodFlag;
            printMessage(stopwatchOuter + " construct", methodFlag + ".init");
        }

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
            super.complete("completed");
        }

        @Override
        public String get(long timeout, TimeUnit unit) {
            try {
                return super.get(timeout, unit);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
