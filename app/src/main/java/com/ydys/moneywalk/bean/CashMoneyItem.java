package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

public class CashMoneyItem {
    @SerializedName("is_new_people")
    private int isNewPeople;//是否新人专享 1是 0否
    private double amount;//提现金额
    @SerializedName("need_gold")
    private int needGold;

    private boolean isSelected;

    public int getIsNewPeople() {
        return isNewPeople;
    }

    public void setIsNewPeople(int isNewPeople) {
        this.isNewPeople = isNewPeople;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getNeedGold() {
        return needGold;
    }

    public void setNeedGold(int needGold) {
        this.needGold = needGold;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
