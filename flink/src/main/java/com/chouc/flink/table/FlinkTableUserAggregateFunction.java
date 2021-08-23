package com.chouc.flink.table;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.types.Row;

import static org.apache.flink.table.annotation.HintFlag.TRUE;

@FunctionHint(input = @DataTypeHint("STRING"),accumulator = @DataTypeHint(bridgedTo = FlinkTableUserAggregateACC.class, allowRawGlobally = TRUE) ,output = @DataTypeHint("ROW< min INT, max INT >"))
public class FlinkTableUserAggregateFunction extends AggregateFunction<Row, FlinkTableUserAggregateACC> {
    public void accumulate(FlinkTableUserAggregateACC acc, String strValue) {
        int value = Integer.parseInt(strValue);
        if (value < acc.min) {
            acc.min = value;
        }
        if (value > acc.max) {
            acc.max = value;
        }
    }

    @Override
    public FlinkTableUserAggregateACC createAccumulator() {
        return new FlinkTableUserAggregateACC();
    }

    public void resetAccumulator(FlinkTableUserAggregateACC acc) {
        acc.min = Integer.MAX_VALUE;
        acc.max = Integer.MIN_VALUE;
    }

    @Override
    public Row getValue(FlinkTableUserAggregateACC acc) {
        return Row.of(acc.min, acc.max);
    }

}
