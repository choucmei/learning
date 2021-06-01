package com.chouc.flink.lagou.lesson31pvuv_step1_etl_kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;

import java.util.Properties;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkEtlKafkaDemo
 * @Package com.chouc.flink.lagou.lesson31etl_kafka
 * @Description:
 * @date 2020/11/12
 */
public class FlinkEtlKafkaDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        env.enableCheckpointing(5000);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
//        FlinkKafkaConsumer<String> source = new FlinkKafkaConsumer<String>("flink_learning",new SimpleStringSchema(),properties);
        DataStreamSource<String> sourceDs = env
                .addSource(new SourceFunction<String>() {
                  @Override
                  public void run(SourceContext<String> ctx) throws Exception {
                      for (int i = 0; i < 100; i++) {
                          JSONObject jsonObject = new JSONObject();
                          jsonObject.put("user_id", "" + i);
                          jsonObject.put("timestamp", (System.currentTimeMillis() / 1000) - 1000);
                          jsonObject.put("action", "CLICK");
                          Thread.sleep(2000);
                          ctx.collect(jsonObject.toJSONString());
                          System.out.println("i:" + i);
                      }
                  }

                  @Override
                  public void cancel() {

                  }
              }
        );
        // sink
        sourceDs.process(new UserActionProcessFunction())
                .addSink(new FlinkKafkaProducer<String>("localhost:9092", "flink_learning", new SimpleStringSchema()));
        env.execute();
    }
}

class UserActionFilter implements FilterFunction<String> {
    @Override
    public boolean filter(String input) throws Exception {
        return input.contains("CLICK") && input.startsWith("{") && input.endsWith("}");
    }
}

class MyFlatMapFunction implements FlatMapFunction<String, String> {

    @Override
    public void flatMap(String input, Collector<String> out) throws Exception {
        JSONObject jsonObject = JSON.parseObject(input);
        String user_id = jsonObject.getString("user_id");
        String action = jsonObject.getString("action");
        Long timestamp = jsonObject.getLong("timestamp");
        if (!StringUtils.isEmpty(user_id) || !StringUtils.isEmpty(action)) {
            ClickEntity userClick = new ClickEntity();
            userClick.setUserId(user_id);
            userClick.setTimestamp(timestamp);
            userClick.setAction(action);
            out.collect(JSON.toJSONString(userClick));
        }
    }
}

class UserActionProcessFunction extends ProcessFunction<String, String> {

    @Override
    public void processElement(String input, Context ctx, Collector<String> out) throws Exception {
        System.out.println(input);
        if (!input.contains("CLICK") || !input.startsWith("{") || !input.endsWith("}")) {
            return;
        }
        JSONObject jsonObject = JSON.parseObject(input);
        String user_id = jsonObject.getString("user_id");
        String action = jsonObject.getString("action");
        Long timestamp = jsonObject.getLong("timestamp");
        if (!StringUtils.isEmpty(user_id) || !StringUtils.isEmpty(action)) {
            ClickEntity userClick = new ClickEntity();
            userClick.setUserId(user_id);
            userClick.setTimestamp(timestamp);
            userClick.setAction(action);
            out.collect(JSON.toJSONString(userClick));
        }
    }
}