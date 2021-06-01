package com.chouc.flink.table.demo1;

/**
 * @author chouc
 * @version V1.0
 * @Title: WC
 * @Package com.chouc.flink.table.demo1
 * @Description:
 * @date 2021/4/6
 */
public class WC {
    public String word;
    public long frequency;

    public WC() {
    }

    public WC(String word, long frequency) {
        this.word = word;
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "WC " + word + " " + frequency;
    }
}
