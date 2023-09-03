package com.demo.provider.manager.dubbo;

import com.demo.dubbo.tri.grpc.DubboGreeterProtoServiceTriple;
import com.demo.dubbo.tri.grpc.GreeterReply;
import com.demo.dubbo.tri.grpc.GreeterRequest;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

@DubboService(group ="triple", protocol = "tri")
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
        return new StreamObserver<GreeterRequest>() {
            private StringBuilder sb = new StringBuilder();

            @Override
            public void onNext(GreeterRequest data) {
                sb.append("hello, ").append(data.getName()).append("; ");
                System.out.println(data + " 执行了triple服务");
                responseObserver.onNext(GreeterReply.newBuilder()
                        .setMessage("hello, " + data.getName())
                        .build());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                responseObserver.onError(new IllegalStateException("Stream err"));
            }

            @Override
            public void onCompleted() {
                System.out.println("[greetStream] onCompleted");
                responseObserver.onNext(GreeterReply.newBuilder()
                        .setMessage(sb.toString())
                        .build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void greetServerStream(GreeterRequest request, StreamObserver<GreeterReply> responseObserver) {
        for (int i = 10; i > 0; i--) {
            long start = System.currentTimeMillis();
            responseObserver.onNext(GreeterReply.newBuilder()
                    .setMessage(request.getName() + "--" + i)
                    .build());
            long onNextEnd = System.currentTimeMillis();
            long sleepEnd = 0;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                sleepEnd = System.currentTimeMillis();
            }
            long nextDiff = onNextEnd - start;
            long sleepDiff = sleepEnd - onNextEnd;
            System.out.println("onNext diff:" + nextDiff + " sleep diff:" + sleepDiff);
        }
        responseObserver.onCompleted();
    }

}
