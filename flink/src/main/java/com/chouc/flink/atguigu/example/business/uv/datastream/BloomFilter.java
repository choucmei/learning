package com.chouc.flink.atguigu.example.business.uv.datastream;

import java.io.Serializable;

public class BloomFilter implements Serializable {
    public BloomFilter(int cap) {
        this.cap = cap;
    }

    private int cap;

    public Long hash(String value, int seed) {
        Long result = 0l;
        for (int i = 0; i < seed; i++) {
            result = result * seed + value.charAt(i);
        }
        return (cap - 1) & result;
    }
}
