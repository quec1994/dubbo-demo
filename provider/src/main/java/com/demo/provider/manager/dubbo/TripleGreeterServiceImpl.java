package com.demo.provider.manager.dubbo;

import com.demo.dubbo.tri.grpc.DubboGreeterProtoServiceTriple;
import com.demo.dubbo.tri.grpc.GreeterReply;
import com.demo.dubbo.tri.grpc.GreeterRequest;
import com.google.common.base.Stopwatch;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

import java.util.concurrent.TimeUnit;

@DubboService(group = "triple", protocol = "tri")
public class TripleGreeterServiceImpl extends DubboGreeterProtoServiceTriple.GreeterProtoServiceImplBase {

    @Override
    public GreeterReply greet(GreeterRequest request) {
        System.out.println(request + " 执行了triple服务");
        URL url = RpcContext.getServiceContext().getUrl();
        String greeting = String.format("%s：%s, Hello %s",
                url.getProtocol(), url.getPort(), request.getName());
        return GreeterReply.newBuilder().setMessage(greeting).build();
    }

    @Override
    public StreamObserver<GreeterRequest> greetStream(StreamObserver<GreeterReply> responseObserver) {
        StreamObserver<GreeterRequest> requestStreamObserver = new StreamObserver<GreeterRequest>() {
            private final Stopwatch stopwatch = Stopwatch.createStarted();
            private final StringBuilder sb = new StringBuilder();

            @Override
            public void onNext(GreeterRequest data) {
                printMessage(data.getName() + " 执行了triple服务", "greetStream.onNext", stopwatch);
                sleep(2);
                sb.append("hello, ").append(data.getName()).append("; ");
                responseObserver.onNext(GreeterReply.newBuilder()
                        .setMessage(stopwatch + " hello, " + data.getName())
                        .build());
            }

            @Override
            public void onError(Throwable throwable) {
                printMessage(throwable, "greetStream.onError", stopwatch);
                responseObserver.onError(new IllegalStateException("Stream err"));
            }

            @Override
            public void onCompleted() {
                printMessage("completed", "greetStream.onCompleted", stopwatch);
                sleep(2);
                sb.insert(0, stopwatch + " ");
                responseObserver.onNext(GreeterReply.newBuilder()
                        .setMessage(sb.toString())
                        .build());
                responseObserver.onCompleted();
            }
        };
        return requestStreamObserver;
    }

    @Override
    public void greetServerStream(GreeterRequest request, StreamObserver<GreeterReply> responseObserver) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 10; i > 0; i--) {
            Stopwatch stopwatchT = Stopwatch.createStarted();
            sleep(1);
            String sleepDiff = stopwatchT.toString();
            stopwatchT.reset();
            responseObserver.onNext(buildGreeterReply(request, stopwatch, i));
            String onNextDiff = stopwatchT.toString();
            printMessage("sleep diff:" + sleepDiff + " onNext diff:" + onNextDiff, "greetServerStream", stopwatch);
        }
        responseObserver.onCompleted();
        printMessage("end", "greetServerStream", stopwatch);
    }

    private static GreeterReply buildGreeterReply(GreeterRequest request, Stopwatch stopwatch, int i) {
        return GreeterReply.newBuilder()
                .setMessage(stopwatch.toString() + " " + request.getName() + "--" + i)
                .build();
    }

    private void printMessage(Throwable throwable, String methodName, Stopwatch stopwatch) {
        printMessage("请求流写入了异常", methodName, stopwatch);
        throwable.printStackTrace();
    }

    private void printMessage(String message, String methodName, Stopwatch stopwatch) {
        System.out.println(Thread.currentThread().getName() + " " + stopwatch.toString() + " " + methodName + "：" + message);
    }

    private static void sleep(int timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
