package com.chouc.flink.atguigu.example.business.topn;

public class ClickEvent {
    private String userId;
    private Long timestamp;
    private String adGroup;
    private String pid;
    private int noclick;
    private int click;

    public ClickEvent() {
    }

    public ClickEvent(String userId, String adGroup, String pid, int noclick, int click, Long timestamp) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.adGroup = adGroup;
        this.pid = pid;
        this.noclick = noclick;
        this.click = click;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setAdGroup(String adGroup) {
        this.adGroup = adGroup;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setNoclick(int noclick) {
        this.noclick = noclick;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public String getUserId() {
        return userId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getAdGroup() {
        return adGroup;
    }

    public String getPid() {
        return pid;
    }

    public int getNoclick() {
        return noclick;
    }

    public int getClick() {
        return click;
    }

    @Override
    public String toString() {
        return userId + "," + adGroup + "," + pid + "," + noclick + "," + click;
    }
}
