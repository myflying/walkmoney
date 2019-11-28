package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/1/25.
 */
public class UserInfo {
    private String id;
    private String mobile;
    private String nickname;//用户昵称
    private String face;//用户头像
    private int gold;//用户金币
    private double amount;//用户金币对应金额
    private int status;//账号状态(1正常，2封号)
    @SerializedName("cash_out_status")
    private int cashOutStatus;//提现封禁状态 1可以提现 2禁止提现
    @SerializedName("is_bind")
    private String isBind; //该设备是否绑定微信或者手机号 1是 0否

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCashOutStatus() {
        return cashOutStatus;
    }

    public void setCashOutStatus(int cashOutStatus) {
        this.cashOutStatus = cashOutStatus;
    }

    public String getIsBind() {
        return isBind;
    }

    public void setIsBind(String isBind) {
        this.isBind = isBind;
    }
}

