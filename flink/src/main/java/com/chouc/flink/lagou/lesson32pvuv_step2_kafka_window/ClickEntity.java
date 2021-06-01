package com.chouc.flink.lagou.lesson32pvuv_step2_kafka_window;

/**
 * @author chouc
 * @version V1.0
 * @Title: ClickDemo
 * @Package com.chouc.flink.lagou.lesson31etl_kafka
 * @Description:
 * @date 2020/11/12
 */
public class ClickEntity {
    private String userId;
    private Long timestamp;
    private String action;

    public ClickEntity() {
    }

    public ClickEntity(String userId, Long timestamp, String action) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.action = action;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

enum UserAction{
    //点击
    CLICK("CLICK"),
    //购买
    PURCHASE("PURCHASE"),
    //其他
    OTHER("OTHER");
    private String action;
    UserAction(String action) {
        this.action = action;
    }
}
