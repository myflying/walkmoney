package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CashRecordInfo implements Serializable {
    private double money;//提现金额
    private int status;//提现状态 1已到账 2提现中
    @SerializedName("add_time")
    private String addTime;//时间
    @SerializedName("finish_time")
    private String finishTime;
    @SerializedName("order_sn")
    private String orderSn;
    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }
}
