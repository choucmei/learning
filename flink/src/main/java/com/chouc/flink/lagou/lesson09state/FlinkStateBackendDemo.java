package com.chouc.flink.lagou.lesson09state;

import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import static org.apache.flink.runtime.state.memory.MemoryStateBackend.DEFAULT_MAX_STATE_SIZE;

/**
 * @author chouc
 * @version V1.0
 * @Title: StateBackendDemo
 * @Package com.chouc.flink.lagou.lession09state
 * @Description:
 * @date 2020/9/20
 */
public class FlinkStateBackendDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        // Memeory StateBackend
        env.setStateBackend(new MemoryStateBackend(DEFAULT_MAX_STATE_SIZE,false));
        // FileSystem StateBackend
        env.setStateBackend(new FsStateBackend("hdfs://namenode:40010/flink/checkpoints", false));
    }
}
