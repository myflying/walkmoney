package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyWalletWapper {
    private int gold;//金币余额
    private double money;//金币余额-对应金额（元）
    @SerializedName("count_gold")
    private int countGold;//累计收益（截止昨日）
    @SerializedName("exchange_rate")
    private int exchangeRate;//金币兑换率 -（1元人民币）
    @SerializedName("to_day_gold")
    private int toDayGold;//今日金币

    private List<GoldDayInfo> list;

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getCountGold() {
        return countGold;
    }

    public void setCountGold(int countGold) {
        this.countGold = countGold;
    }

    public int getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(int exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public int getToDayGold() {
        return toDayGold;
    }

    public void setToDayGold(int toDayGold) {
        this.toDayGold = toDayGold;
    }

    public List<GoldDayInfo> getList() {
        return list;
    }

    public void setList(List<GoldDayInfo> list) {
        this.list = list;
    }
}
