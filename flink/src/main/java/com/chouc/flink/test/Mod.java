package com.chouc.flink.test;

import org.apache.flink.table.functions.ScalarFunction;

/**
 * @Author choucmei
 * @create 2022/5/31
 */
public class Mod extends ScalarFunction {

    public Integer eval(Long id, int size) {
        long l = (int) (id % size);
        return (int) (id % size);
    }

}
