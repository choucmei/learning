package com.chouc.flink.lagou.lesson07cache_crash_restart_parallize;


import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.types.StringValue;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkCacheDemo
 * @Package com.chouc.flink.lagou.lession08cache_crash_restart_parallize
 * @Description:
 * @date 2020/8/7
 */
public class FlinkCacheDemo {
    final static Logger logger = Logger.getLogger(FlinkCacheDemo.class);
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.registerCachedFile("/Users/chouc/Work/IdeaProjects/atguigu/learning-flink/src/main/resources/example/cache/cache_tmp.txt","tmp");
        DataSource<StringValue> dataSource = env.readTextFileWithValue("/Users/chouc/Work/IdeaProjects/atguigu/learning-flink/src/main/resources/example/cache/tmp.txt");
        dataSource.map(new RichMapFunction<StringValue, String>() {
            Map<String,String> cache = new HashMap<String,String>();
            @Override
            public void open(Configuration parameters) throws Exception {
                File file = getRuntimeContext().getDistributedCache().getFile("tmp");
                List<String> tmp = FileUtils.readLines(file);
                cache = tmp.stream().map(v -> {
                    String[] array = v.split(",");
                    Tuple2<String,String> tuple = Tuple2.of(array[0],array[1]);
                    return tuple;
                }).collect(Collectors.toMap(t->t.f0,t->t.f1));
                logger.info("*********** cache loaded ************");
            }

            @Override
            public String map(StringValue value) throws Exception {
                String s = value.getValue();
                return cache.getOrDefault(s,"null");
            }
        }).print();

    }
}
