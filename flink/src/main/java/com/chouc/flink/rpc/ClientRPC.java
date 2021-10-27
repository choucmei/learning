package com.chouc.flink.rpc;

import akka.actor.ActorSystem;
import org.apache.flink.runtime.akka.AkkaUtils;
import org.apache.flink.runtime.rpc.RpcService;
import org.apache.flink.runtime.rpc.akka.AkkaRpcService;
import org.apache.flink.runtime.rpc.akka.AkkaRpcServiceConfiguration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ClientRPC {
    public static void main(String[] args) {
        ActorSystem defaultActorSystem = AkkaUtils.createDefaultActorSystem();
        RpcService rpcService = new AkkaRpcService(defaultActorSystem, AkkaRpcServiceConfiguration.defaultConfiguration());
        CompletableFuture<HelloGateway> connect = rpcService.connect(args[0], HelloGateway.class);
        try {
            System.out.println(connect.get().hello().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            rpcService.stopService();
        }

    }
}
