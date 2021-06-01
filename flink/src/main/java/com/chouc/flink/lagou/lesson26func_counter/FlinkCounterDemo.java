package com.chouc.flink.lagou.lesson26func_counter;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkCounterDemo
 * @Package com.chouc.flink.lagou.lession26func
 * @Description:
 * @date 2020/11/4
 */
public class FlinkCounterDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStreamSource<String> source = env.addSource(new SocketTextStreamFunction("localhost", 9999, "\n", 3));
        SingleOutputStreamOperator<String> dataStream = source.map(new RichMapFunction<String, String>() {
            //第一步：定义累加器
            private IntCounter intCounter = new IntCounter();

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                //第二步：注册累加器
                getRuntimeContext().addAccumulator("count", this.intCounter);
            }

            @Override
            public String map(String value) throws Exception {
                if (value != null && !value.equals("")) {
                    //第三步：累加
                    this.intCounter.add(1);
                }
                return value;
            }
        });
        dataStream.print();
        JobExecutionResult result = env.execute();;
        System.out.println("******************");
        //第四步：结束后输出总量；如果不需要结束后持久化，可以省去
        System.out.println("result:"+result);
        Object accResult = result.getAccumulatorResult("count");
        System.out.println("################");
        System.out.println("累加器计算结果:" + accResult);
        System.out.println("================");
    }
}
