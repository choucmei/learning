package com.chouc.flink.rpc;

import org.apache.flink.runtime.rpc.RpcGateway;

import java.util.concurrent.CompletableFuture;

public interface HelloGateway extends RpcGateway {
    CompletableFuture<String> hello();
}
