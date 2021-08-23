package com.chouc.flink.table;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.table.functions.TableFunction;

public class FlinkTableUserTableFunction extends TableFunction<Tuple2<String, String>> {
    public void eval(String str) {
        try {
            String[] s = str.split(" ");
            collect(Tuple2.of(s[0], s[1]));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
