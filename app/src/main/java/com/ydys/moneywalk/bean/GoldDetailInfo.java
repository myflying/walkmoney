package com.ydys.moneywalk.bean;

public class GoldDetailInfo {
    private String id;
    private long goldDate;
    private int goldType;
    private int goldNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getGoldDate() {
        return goldDate;
    }

    public void setGoldDate(long goldDate) {
        this.goldDate = goldDate;
    }

    public int getGoldType() {
        return goldType;
    }

    public void setGoldType(int goldType) {
        this.goldType = goldType;
    }

    public int getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(int goldNum) {
        this.goldNum = goldNum;
    }
}
