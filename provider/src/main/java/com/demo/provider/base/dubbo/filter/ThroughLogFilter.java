package com.demo.provider.base.dubbo.filter;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

@Activate(group = PROVIDER)
public class ThroughLogFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = RpcContext.getServiceContext().getUrl();
        System.out.printf("收到请求，协议：%s 端口：%s\n", url.getProtocol(), url.getPort());
        return invoker.invoke(invocation);
    }

}
