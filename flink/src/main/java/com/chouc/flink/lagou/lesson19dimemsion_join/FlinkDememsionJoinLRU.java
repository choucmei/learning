package com.chouc.flink.lagou.lesson19dimemsion_join;

import com.stumbleupon.async.Callback;
import org.apache.flink.calcite.shaded.com.google.common.cache.Cache;
import org.apache.flink.calcite.shaded.com.google.common.cache.CacheBuilder;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.hbase.async.GetRequest;
import org.hbase.async.HBaseClient;
import org.hbase.async.KeyValue;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkDememsionJoinLRU
 * @Package com.chouc.flink.lagou.lession19dimemsion_join
 * @Description:
 * @date 2020/10/9
 */
public class FlinkDememsionJoinLRU {
    class LRU extends RichAsyncFunction<String,String> {
        String table = "info";
        Cache<String, String> cache = null;
        private HBaseClient client = null;
        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            //创建hbase客户端
            client = new HBaseClient("127.0.0.1","7071");
            cache = CacheBuilder.newBuilder()
                    //最多存储10000条
                    .maximumSize(10000)
                    //过期时间为1分钟
                    .expireAfterWrite(60, TimeUnit.SECONDS)
                    .build();
        }
        @Override
        public void asyncInvoke(String input, ResultFuture resultFuture) throws Exception {

            //读缓存
            String query = cache.getIfPresent("cityId");
            //如果缓存获取失败再从hbase获取维度数据
            if(query != null){
                // join
            }else {
                client.get(new GetRequest(table,String.valueOf("cityId"))).addCallback((Callback<String, ArrayList<KeyValue>>) arg -> {
                    // join
                    return null;
                });
            }
        }
    }
}
