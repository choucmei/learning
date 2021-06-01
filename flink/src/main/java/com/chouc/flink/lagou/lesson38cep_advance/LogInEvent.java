package com.chouc.flink.lagou.lesson38cep_advance;

/**
 * @author chouc
 * @version V1.0
 * @Title: LogInEvent
 * @Package com.chouc.flink.lagou.lesson38cep_advance
 * @Description:
 * @date 2020/11/22
 */
public class LogInEvent {
    private Long id;
    private String isSuccess;
    private Long timestamp;

    public LogInEvent() {
    }

    public LogInEvent(Long id, String isSuccess, Long timestamp) {
        this.id = id;
        this.isSuccess = isSuccess;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LogInEvent{" +
                "id=" + id +
                ", isSuccess='" + isSuccess + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
