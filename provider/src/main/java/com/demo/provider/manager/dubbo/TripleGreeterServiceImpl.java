package com.demo.provider.manager.dubbo;

import com.demo.proto.dubbo.tri.DubboGreeterProtoServiceTriple;
import com.demo.proto.dubbo.tri.GreeterReply;
import com.demo.proto.dubbo.tri.GreeterRequest;
import com.google.common.base.Stopwatch;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@DubboService(group = "triple", protocol = "tri")
public class TripleGreeterServiceImpl extends DubboGreeterProtoServiceTriple.GreeterProtoServiceImplBase {

    @Override
    public GreeterReply greet(GreeterRequest request) {
        System.out.println(request.getName() + " 执行了triple服务");
        String greeting = String.format("您好 %s！", request.getName());
        return GreeterReply.newBuilder().setMessage(greeting).build();
    }

    @Override
    public StreamObserver<GreeterRequest> greetStream(StreamObserver<GreeterReply> responseObserver) {
        final Stopwatch stopwatchOuter = Stopwatch.createStarted();
        String methodFlag = "greetStream";
        String methodFlagOuter = methodFlag + ".outer";
        printReceivedMessage("received", methodFlagOuter, stopwatchOuter);
        StreamObserver<GreeterRequest> requestStreamObserver = new StreamObserver<GreeterRequest>() {

            {
                printMessage(stopwatchOuter + " construct", methodFlag + ".init");
            }

            private final Stopwatch stopwatch = Stopwatch.createStarted();
            private final StringBuilder sb = new StringBuilder(" [");

            @Override
            public void onNext(GreeterRequest greeterRequest) {
                String methodFlagInternal = methodFlag + ".onNext";
                printReceivedMessage(greeterRequest.getName(), methodFlagInternal, stopwatch);
                sleepAndRunAndPrintSentMessage(2, methodFlagInternal, stopwatch, () -> {
                    sb.append(greeterRequest.getName()).append("; ");
                    responseObserver.onNext(buildGreeterReply(greeterRequest));
                });
            }

            private GreeterReply buildGreeterReply(GreeterRequest greeterRequest) {
                return GreeterReply.newBuilder()
                        .setMessage("Server " + stopwatch + " [" + greeterRequest.getName() + "]")
                        .build();
            }

            @Override
            public void onError(Throwable throwable) {
                printMessage(throwable, methodFlag + ".onError");
                responseObserver.onError(new IllegalStateException("Stream err"));
            }

            @Override
            public void onCompleted() {
                String methodFlagInternal = methodFlag + ".onCompleted";
                printReceivedMessage("completed", methodFlagInternal, stopwatch);
                sleepAndRunAndPrintSentMessage(2, methodFlagInternal, stopwatch,
                        () -> {
                            sb.insert(0, stopwatch).append("]");
                            responseObserver.onNext(GreeterReply.newBuilder()
                                    .setMessage(sb.toString())
                                    .build());
                            responseObserver.onCompleted();
                        });
            }
        };
        sleep(3, methodFlagOuter);
        printEndMessage("end", methodFlagOuter, stopwatchOuter);
        return requestStreamObserver;
    }

    @Override
    public void greetServerStream(GreeterRequest request, StreamObserver<GreeterReply> responseObserver) {
        Stopwatch stopwatchOuter = Stopwatch.createStarted();
        String methodFlag = "greetServerStream";
        String methodFlagOuter = methodFlag + ".outer";
        printReceivedMessage("received", methodFlagOuter, stopwatchOuter);
        sleepAndRunAndPrintForMessage(1, methodFlagOuter, stopwatchOuter,
                () -> responseObserver.onNext(buildGreeterReply(request, stopwatchOuter)));
        responseObserver.onCompleted();
        printEndMessage("end", methodFlagOuter, stopwatchOuter);
    }

    private GreeterReply buildGreeterReply(GreeterRequest greeterRequest, Stopwatch stopwatch) {
        return GreeterReply.newBuilder()
                .setMessage("Server " + stopwatch + " [" + greeterRequest.getName() + "]")
                .build();
    }

    private void sleep(int timeout, String methodFlag) {
        sleepAndRun(timeout, methodFlag, null, null);
    }

    private void sleepAndRunAndPrintForMessage(int timeout, String methodFlag, Stopwatch stopwatch, Runnable runnable) {
        IntStream.rangeClosed(1, 10)
                .forEach((i) -> sleepAndRun(timeout, methodFlag + "-For", stopwatch, runnable));
    }

    private void sleepAndRunAndPrintSentMessage(int timeout, String methodFlagInternal, Stopwatch stopwatch, Runnable runnable) {
        sleepAndRun(timeout, methodFlagInternal + "-Sent", stopwatch, runnable);
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
        printMessage("请求流被写入了异常", methodFlag);
        throwable.printStackTrace();
    }

    private void printEndMessage(String message, String methodFlag, Stopwatch stopwatch) {
        printMessage(stopwatch + " [" + message + "]", methodFlag + "-End");
    }

    private void printReceivedMessage(String message, String methodFlag, Stopwatch stopwatch) {
        printMessage(stopwatch + " [" + message + "]", methodFlag + "-Received");
    }

    private void printMessage(String message, String methodFlag) {
        String now = DateTimeFormatter.ISO_TIME.format(LocalTime.now());
        System.out.println(now + " [" + Thread.currentThread().getName() + "] " + methodFlag + "：" + message);
    }

}
