package com.chouc.flink.atguigu.example.business.topn.datastream;

import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

public class AdAggWindowFunc  implements WindowFunction<Long, AdCountWithWindow, String, TimeWindow> {

    @Override
    public void apply(String s, TimeWindow window, Iterable<Long> input, Collector<AdCountWithWindow> out) throws Exception {
        Long count = input.iterator().next();
        out.collect(new AdCountWithWindow(s, count, window.getEnd()));
    }
}
