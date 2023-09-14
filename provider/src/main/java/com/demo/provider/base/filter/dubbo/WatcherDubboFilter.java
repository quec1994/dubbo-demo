package com.demo.provider.base.filter.dubbo;

import com.google.common.base.Stopwatch;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

@Activate(group = PROVIDER)
public class WatcherDubboFilter implements Filter, BaseFilter.Listener {
    private static final String STOPWATCH_KEY = WatcherDubboFilter.class.getName() + "#Stopwatch";

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = RpcContext.getServiceContext().getUrl();
        System.out.printf("WatcherDubboFilter-收到请求，协议：%s 端口：%s\n", url.getProtocol(), url.getPort());

        invocation.put(STOPWATCH_KEY, Stopwatch.createStarted());
        return invoker.invoke(invocation);
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        Stopwatch stopwatch = ((Stopwatch) invocation.get(STOPWATCH_KEY));
        System.out.printf("WatcherDubboFilter-响应请求，处理耗时：%s\n", stopwatch);
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
        Stopwatch stopwatch = ((Stopwatch) invocation.get(STOPWATCH_KEY));
        System.out.printf("WatcherDubboFilter-请求处理异常，处理耗时：%s\n", stopwatch);
    }

}
