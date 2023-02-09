package com.chouc.flink.test;

import org.apache.flink.table.functions.TableFunction;

/**
 * @Author choucmei
 * @create 2022/5/31
 */
public class StatusMapperUDF extends TableFunction<String> {

    public void eval(String status) {
        if (status.equals("5")) {
            collect("等级4");
        } else {
            if (status.equals("1")) {
                collect("等级1");
            } else if (status.equals("2")) {
                collect("等级2");
            } else if (status.equals("3")) {
                collect("等级3");
            }
        }

    }

}
