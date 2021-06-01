package com.chouc.flink.lagou.lession01chain_shareslotgroup;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkChainDemo
 * @Package com.chouc.flink.lagou.lession01chain
 * @Description:
 * @date 2021/2/4
 */
public class FlinkChainDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        /**
         *   disable all chain of the opertors
         *   env.disableOperatorChaining();
         */
//        env.disableOperatorChaining();

        SingleOutputStreamOperator<String> streamOperator = env.addSource(new SocketTextStreamFunction("localhost",9999,"\t",1000))
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String value, Collector<String> out) throws Exception {
                        for (String s:value.split(" ")){
                            out.collect(s);
                        }
                    }
                })
//                .disableChaining()
//                .startNewChain()
                ;


        /**
         *
         *  disableChaining()
         *
         *  cut next operator all chains
         *
         */

        /**
         *
         * startNewChain()
         *
         * cut current chains ， 1 -> 2 chains
         *
         */

        SingleOutputStreamOperator<Tuple2<String,Integer>> mapStream = streamOperator.map(new MapFunction<String, Tuple2<String,Integer>>() {
            @Override
            public Tuple2<String, Integer> map(String v) throws Exception {
                return Tuple2.of(v,1);
            }
        });



        mapStream.keyBy(0)
                .sum(1)
                .print();
        env.execute();
    }




}
