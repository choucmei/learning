package com.chouc.flink.lagou.lesson38cep_advance;

import java.io.Serializable;

/**
 * @author chouc
 * @version V1.0
 * @Title: PayEvent
 * @Package com.chouc.flink.lagou.lesson38cep_advance
 * @Description:
 * @date 2020/11/22
 */
public class PayEvent implements Serializable {
    private Long userId;
    private String pay;
    private Long timestamp;

    public PayEvent() {
    }

    public PayEvent(Long userId, String pay, Long timestamp) {
        this.userId = userId;
        this.pay = pay;
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PayEvent{" +
                "userId=" + userId +
                ", pay='" + pay + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
