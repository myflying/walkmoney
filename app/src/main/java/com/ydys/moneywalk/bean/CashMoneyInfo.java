package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

public class CashMoneyInfo {
    @SerializedName("user_id")
    private String userId;//用户id
    private double money;//提现金额
    @SerializedName("is_new_people")
    private int isNewPeople;//是否新用户专享 1是 0否

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getIsNewPeople() {
        return isNewPeople;
    }

    public void setIsNewPeople(int isNewPeople) {
        this.isNewPeople = isNewPeople;
    }
}
