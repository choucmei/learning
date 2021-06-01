package com.chouc.flink.lagou.lesson28topn_demo;

/**
 * @author chouc
 * @version V1.0
 * @Title: OrderDetail
 * @Package com.chouc.flink.lagou.lession28topn_demo
 * @Description:
 * @date 2020/11/5
 */
public class OrderDetail {
    private String userId; //下单用户id
    private String itemId; //商品id
    private String citeName;//用户所在城市
    private Double price;//订单金额
    private String timeStamp;//下单时间

    public OrderDetail() {
    }

    public OrderDetail(String userId, String itemId, String citeName, Double price, String timeStamp) {
        this.userId = userId;
        this.itemId = itemId;
        this.citeName = citeName;
        this.price = price;
        this.timeStamp = timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getCiteName() {
        return citeName;
    }

    public Double getPrice() {
        return price;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setCiteName(String citeName) {
        this.citeName = citeName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
