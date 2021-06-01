package com.chouc.flink.state.case_topn;

import java.util.Date;

/**
 * @author chouc
 * @version V1.0
 * @Title: RequestEntity
 * @Package com.chouc.flink.state.case_topn
 * @Description:
 * @date 2021/3/9
 */
public class RequestEntity {
    private String ip;
    private String userId;
    private Date date;
    private String requestType;
    private String url;

    public RequestEntity(String ip, String userId, String requestType, String url, Date date) {
        this.ip = ip;
        this.userId = userId;
        this.date = date;
        this.requestType = requestType;
        this.url = url;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
