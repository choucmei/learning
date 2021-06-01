package com.chouc.flink.lagou.lesson08window_time_watermark;

/**
 * @author chouc
 * @version V1.0
 * @Title: Lesssion08DataEntity
 * @Package com.chouc.flink.lagou.lession08window_time_watermark
 * @Description:
 * @date 2020/9/6
 */
public class Lesssion08DataEntity {
    public String value;
    public Long t;

    public Lesssion08DataEntity() {
    }

    public Lesssion08DataEntity(String value, Long t) {
        this.value = value;
        this.t = t;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getT() {
        return t;
    }

    public void setT(Long t) {
        this.t = t;
    }
}
