package com.chouc.flink.state.case_connect;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author chouc
 * @version V1.0
 * @Title: RedisSource
 * @Package com.chouc.flink.state.case_connect
 * @Description:
 * @date 2021/3/31
 */
public class RedisSource extends RichParallelSourceFunction<Tuple2<String, String>> {

    private String host;
    private int port;
    private JedisPool jedis = null;
    private Boolean isRunning = true;
    private HashSet<String> cache = null;

    public RedisSource(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        this.jedis = new JedisPool(this.host, this.port);
        this.cache = new HashSet<>();
        super.open(parameters);
    }

    @Override
    public void run(SourceContext ctx) throws Exception {
        Set<String> values = jedis.getResource().keys("AREA_*");
        HashMap<String, String> kVMap = new HashMap<String, String>();
        while (isRunning) {
            try {
                kVMap.clear();
                Map<String, String> areas = jedis.getResource().hgetAll("area");
                for (Map.Entry<String, String> entry : areas.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    for (String split : value.split(",")) {
                        if (!cache.contains(split)) {
                            ctx.collect(Tuple2.of(split, key));
                            cache.add(split);
                        }
                    }
                }
                Thread.sleep(1000 * 3);
            } catch (JedisConnectionException e) {
                System.out.println("redis connect error");
            }
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
