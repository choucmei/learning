package com.chouc.flink.table;

import org.apache.flink.table.functions.TableFunction;

public class FlinkTableUserFlatMapFunction extends TableFunction<String> {
    public void eval(String str) {
        try {
            for (String s : str.split(" ")) {
                collect(s);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
