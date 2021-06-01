package com.chouc.flink.lagou.lesson26func_counter;


import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkWindowReduceDemo
 * @Package com.chouc.flink.lagou.lession26func
 * @Description:
 * @date 2020/11/3
 */
public class FlinkWindowReduceDemo {
    public static void main(String[] args) throws Exception {
        Tuple2[] courses = new Tuple2[]{
                Tuple2.of("张三", 100),
                Tuple2.of("李四", 80),
                Tuple2.of("张三", 80),
                Tuple2.of("李四", 95),
                Tuple2.of("张三", 90),
                Tuple2.of("李四", 100),
        };
        ExecutionEnvironment env = ExecutionEnvironment.createLocalEnvironment();
        DataSource<Tuple2<String, Integer>> dataSet = env.fromElements(courses);
//        dataSet.mapPartition()
        dataSet.groupBy(0).reduce(new ReduceFunction<Tuple2<String, Integer>>() {
            @Override
            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                return Tuple2.of(value1.f0, value1.f1 + value2.f1);
            }
        }).print();
    }
}
