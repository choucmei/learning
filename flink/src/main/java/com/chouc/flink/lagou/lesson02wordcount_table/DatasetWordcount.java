package com.chouc.flink.lagou.lesson02wordcount_table;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: DatasetWordcount
 * @Package com.chouc.flink.lagou.lession02wordcount_table
 * @Description:
 * @date 2020/8/15
 */
public class DatasetWordcount {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> dataSource = env.readTextFile("/Users/chouc/Work/IdeaProjects/atguigu/learning-flink/src/main/resources/example/cache/cache_tmp.txt");
        FlatMapOperator<String, Tuple2<String, Integer>> flatMapSource = dataSource.flatMap(new FlatMapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                for (String s : value.split(",")) {
                    out.collect(Tuple2.of(s,1));
                }
            }
        });
        flatMapSource.groupBy(0).sum(1).print();
    }
}
