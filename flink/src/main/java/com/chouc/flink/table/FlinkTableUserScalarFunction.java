package com.chouc.flink.table;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.types.Row;

@FunctionHint(input = @DataTypeHint("STRING"))
@FunctionHint(output = @DataTypeHint("ROW<s STRING, l INT >"))
public class FlinkTableUserScalarFunction extends ScalarFunction {
    public Row eval(String a) {
        return Row.of("ch_"+a, a.length());
    }
}