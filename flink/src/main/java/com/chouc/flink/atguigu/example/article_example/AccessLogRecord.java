package com.chouc.flink.atguigu.example.article_example;

/**
 * @author chouc
 * @version V1.0
 * @Title: AccessLogRecord
 * @Package com.chouc.flink.atguigu.example.article_example
 * @Description:
 * @date 2020/9/1
 */
public class AccessLogRecord {
    public String clientIpAddress; // 客户端ip地址
    public String clientIdentity; // 客户端身份标识,该字段为 `-`
    public String remoteUser; // 用户标识,该字段为 `-`
    public String dateTime; //日期,格式为[day/month/yearhourminutesecond zone]
    public String request; // url请求,如：`GET /foo ...`
    public String httpStatusCode; // 状态码，如：200; 404.
    public String bytesSent; // 传输的字节数，有可能是 `-`
    public String referer; // 参考链接,即来源页
    public String userAgent;  // 浏览器和操作系统类型

    public AccessLogRecord() {
    }

    public AccessLogRecord(String clientIpAddress, String clientIdentity, String remoteUser, String dateTime, String request, String httpStatusCode, String bytesSent, String referer, String userAgent) {
        this.clientIpAddress = clientIpAddress;
        this.clientIdentity = clientIdentity;
        this.remoteUser = remoteUser;
        this.dateTime = dateTime;
        this.request = request;
        this.httpStatusCode = httpStatusCode;
        this.bytesSent = bytesSent;
        this.referer = referer;
        this.userAgent = userAgent;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public String getClientIdentity() {
        return clientIdentity;
    }

    public void setClientIdentity(String clientIdentity) {
        this.clientIdentity = clientIdentity;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getBytesSent() {
        return bytesSent;
    }

    public void setBytesSent(String bytesSent) {
        this.bytesSent = bytesSent;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
