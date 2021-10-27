package com.chouc.flink.rpc;

import org.apache.flink.runtime.rpc.RpcEndpoint;
import org.apache.flink.runtime.rpc.RpcService;

import java.util.concurrent.CompletableFuture;

public class HelloRpcEndpoint  extends RpcEndpoint implements HelloGateway{
    protected HelloRpcEndpoint(RpcService rpcService, String endpointId) {
        super(rpcService, endpointId);
    }

    @Override
    public CompletableFuture<String> hello() {
        return CompletableFuture.completedFuture("hello world");
    }
}
