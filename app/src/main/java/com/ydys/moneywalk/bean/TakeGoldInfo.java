package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

public class TakeGoldInfo {

    private String userId;//用户id
    private String taskId;//任务id
    private int gold;//领取金币
    private int isDouble;//是否翻倍
    private int isPlay;
    private int stage;

    private double amount;//用户最新金币余额-对应的人民币余额

    @SerializedName("luck_rest_num")
    private int luckRestNum;

    @SerializedName("request_gold")
    private int requestGold;//请求的金币数

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getIsDouble() {
        return isDouble;
    }

    public void setIsDouble(int isDouble) {
        this.isDouble = isDouble;
    }

    public int getIsPlay() {
        return isPlay;
    }

    public void setIsPlay(int isPlay) {
        this.isPlay = isPlay;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getLuckRestNum() {
        return luckRestNum;
    }

    public void setLuckRestNum(int luckRestNum) {
        this.luckRestNum = luckRestNum;
    }

    public int getRequestGold() {
        return requestGold;
    }

    public void setRequestGold(int requestGold) {
        this.requestGold = requestGold;
    }
}
