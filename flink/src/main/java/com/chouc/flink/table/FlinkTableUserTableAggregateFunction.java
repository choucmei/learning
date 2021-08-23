package com.chouc.flink.table;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.table.functions.TableAggregateFunction;
import org.apache.flink.util.Collector;

public class FlinkTableUserTableAggregateFunction extends TableAggregateFunction<Tuple2<Integer, Integer>, FlinkTableUserTableACC> {
    @Override
    public FlinkTableUserTableACC createAccumulator() {
        return new FlinkTableUserTableACC();
    }


    public void accumulate(FlinkTableUserTableACC acc, String strV) {
        Integer v = Integer.parseInt(strV);
        if (v > acc.first) {
            acc.second = acc.first;
            acc.first = v;
        } else if (v > acc.second) {
            acc.second = v;
        }
    }


    public void mergeACC(FlinkTableUserTableACC acc, Integer v) {
        if (v > acc.first) {
            acc.second = acc.first;
            acc.first = v;
        } else if (v > acc.second) {
            acc.second = v;
        }
    }

    public void merge(FlinkTableUserTableACC acc, java.lang.Iterable<FlinkTableUserTableACC> iterable) {
        for (FlinkTableUserTableACC otherAcc : iterable) {
            mergeACC(acc, otherAcc.first);
            mergeACC(acc, otherAcc.second);
        }
    }

    public void emitValue(FlinkTableUserTableACC acc, Collector<Tuple2<Integer, Integer>> out) {
        // emit the value and rank
        if (acc.first != Integer.MIN_VALUE) {
            out.collect(Tuple2.of(acc.first, 1));
        }
        if (acc.second != Integer.MIN_VALUE) {
            out.collect(Tuple2.of(acc.second, 2));
        }
    }
}
