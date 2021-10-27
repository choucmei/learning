package com.chouc.flink.atguigu.example.business.count;

public class Order {
    String name;
    String country;
    String company;
    String company2;
    String orderProvince;
    Double prices;
    Long times;

    public Order(String name, String country, String company, String company2, String orderProvince, Double prices, Long times) {
        this.name = name;
        this.country = country;
        this.company = company;
        this.company2 = company2;
        this.orderProvince = orderProvince;
        this.prices = prices;
        this.times = times;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany2() {
        return company2;
    }

    public void setCompany2(String company2) {
        this.company2 = company2;
    }

    public String getOrderProvince() {
        return orderProvince;
    }

    public void setOrderProvince(String orderProvince) {
        this.orderProvince = orderProvince;
    }

    public Double getPrices() {
        return prices;
    }

    public void setPrices(Double prices) {
        this.prices = prices;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    @Override
    public String toString() {
        return "Order{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", company='" + company + '\'' +
                ", company2='" + company2 + '\'' +
                ", orderProvince='" + orderProvince + '\'' +
                ", prices=" + prices +
                ", times=" + times +
                '}';
    }
}
