package com.chouc.flink.alarm;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SocketTextStreamFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author chouc
 * @version V1.0
 * @Title: FlinkStreamDemo
 * @Package com.chouc.flink.alarm
 * @Description:
 * @date 2021/2/23
 */
public class FlinkStreamDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(1000);

        // 设置模式为exactly-once （这是默认值）
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 确保检查点之间有至少500 ms的间隔【checkpoint最小间隔】
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
        // 检查点必须在一分钟内完成，或者被丢弃【checkpoint的超时时间】
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        // 同一时间只允许进行一个检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        // 表示一旦Flink处理程序被cancel后，会保留Checkpoint数据，以便根据实际需要恢复到指定的Checkpoint
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);


        StateBackend fsStateBackend = new FsStateBackend("file:///Users/chouc/Work/IdeaProjects/atguigu/learning-flink/src/main/resources/checkpoint/FlinkStreamDemo");
        env.setStateBackend(fsStateBackend);
        DataStreamSource<String> dataStream = env.addSource(new SocketTextStreamFunction("localhost", 9090, "\n", 3));
        dataStream.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                for (String str : value.split(",")) {
                    boolean condition = true;
                    if (condition) {
                        out.collect(Tuple2.of(str, 1));
                    }
                }
            }
        }).keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
            @Override
            public String getKey(Tuple2<String, Integer> value) throws Exception {
                return value.f0;
            }
        }).window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
            .process(new ProcessWindowFunction<Tuple2<String, Integer>, Tuple2<String, Integer>, String, TimeWindow>() {
                MapStateDescriptor<String, Integer> stateDescriptor;
                MapState<String, Integer> state;
                @Override
                public void open(Configuration parameters) throws Exception {
                    super.open(parameters);
                    stateDescriptor = new MapStateDescriptor<String, Integer>("result", TypeInformation.of(new TypeHint<String>() {
                    }), TypeInformation.of(new TypeHint<Integer>() {
                    }));
//                    StateTtlConfig stc = StateTtlConfig.newBuilder(org.apache.flink.api.common.time.Time.seconds(10))
//                            .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
//                            .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
//                            .build();
//                    stateDescriptor.enableTimeToLive(stc);
                    state = getRuntimeContext().getMapState(stateDescriptor);
                    super.open(parameters);
                }

                @Override
                public void process(String s, Context context, Iterable<Tuple2<String, Integer>> values, Collector<Tuple2<String, Integer>> out) throws Exception {
                    values.forEach(value -> {
                        int preValue = 0;
                        try {
                            if (state.get(value.f0) != null) {
                                preValue = state.get(value.f0);
                            }
                            System.out.println("preValue:"+preValue);
                            state.put(value.f0, preValue + value.f1);
                            out.collect(Tuple2.of(value.f0, preValue + value.f1));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }).print();
        env.execute();
    }
}
