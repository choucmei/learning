package com.chouc.flink.rpc;

import akka.actor.ActorSystem;
import org.apache.flink.runtime.akka.AkkaUtils;
import org.apache.flink.runtime.rpc.RpcService;
import org.apache.flink.runtime.rpc.akka.AkkaRpcService;
import org.apache.flink.runtime.rpc.akka.AkkaRpcServiceConfiguration;

public class ServerRPC {
    public static void main(String[] args) {
        ActorSystem defaultActorSystem = AkkaUtils.createDefaultActorSystem();
        RpcService rpcService = new AkkaRpcService(defaultActorSystem, AkkaRpcServiceConfiguration.defaultConfiguration());
        HelloRpcEndpoint helloRpcEndpoint = new HelloRpcEndpoint(rpcService, "hello");
        helloRpcEndpoint.start();
        System.out.println(helloRpcEndpoint.getAddress());
    }
}
