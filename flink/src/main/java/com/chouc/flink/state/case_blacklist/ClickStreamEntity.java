package com.chouc.flink.state.case_blacklist;

import java.util.Date;

/**
 * @author chouc
 * @version V1.0
 * @Title: ClickStreamEntity
 * @Package com.chouc.flink.state.case_blacklist
 * @Description:
 * @date 2021/3/10
 */
public class ClickStreamEntity {
    private String userId;
    private String adId;
    private Date date;

    public ClickStreamEntity(String userId, String adId, Date date) {
        this.userId = userId;
        this.adId = adId;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ClickStreamEntity{" +
                "userId='" + userId + '\'' +
                ", adId='" + adId + '\'' +
                ", date=" + date +
                '}';
    }
}
