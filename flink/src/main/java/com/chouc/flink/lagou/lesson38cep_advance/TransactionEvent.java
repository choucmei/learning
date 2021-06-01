package com.chouc.flink.lagou.lesson38cep_advance;

/**
 * @author chouc
 * @version V1.0
 * @Title: TransactionEvent
 * @Package com.chouc.flink.lagou.lesson38cep_advance
 * @Description:
 * @date 2020/11/22
 */
public class TransactionEvent {
    private String account;
    private Double price;
    private Long timestamp;

    public TransactionEvent() {
    }

    public TransactionEvent(String account, Double price, Long timestamp) {
        this.account = account;
        this.price = price;
        this.timestamp = timestamp;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
