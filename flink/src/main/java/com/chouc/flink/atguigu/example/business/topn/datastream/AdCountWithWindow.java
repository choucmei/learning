package com.chouc.flink.atguigu.example.business.topn.datastream;

import java.io.Serializable;

public class AdCountWithWindow implements Serializable {
    String adGroup;
    Long count;
    Long windowEnd;

    public AdCountWithWindow() {
    }

    public AdCountWithWindow(String adGroup, Long count, Long windowEnd) {
        this.adGroup = adGroup;
        this.count = count;
        this.windowEnd = windowEnd;
    }

    public String getAdGroup() {
        return adGroup;
    }

    public void setAdGroup(String adGroup) {
        this.adGroup = adGroup;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getWindowEnd() {
        return windowEnd;
    }

    public void setWindowEnd(Long windowEnd) {
        this.windowEnd = windowEnd;
    }

    @Override
    public String toString() {
        return "AdCountWithWindow{" +
                "adGroup='" + adGroup + '\'' +
                ", count=" + count +
                ", windowEnd=" + windowEnd +
                '}';
    }
}
