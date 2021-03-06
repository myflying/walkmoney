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
    private int isBind; //该设备是否绑定微信或者手机号 1是 0否

    @SerializedName("bind_wechat")
    private int bindWechat;//是否绑定微信 1是 0否

    @SerializedName("bind_mobile")
    private int bindMobile;//是否绑定手机号 1是 0否

    @SerializedName("show_new")
    private int showNew;//新人金币弹框 1弹 0不弹

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

    public int getIsBind() {
        return isBind;
    }

    public void setIsBind(int isBind) {
        this.isBind = isBind;
    }

    public int getBindWechat() {
        return bindWechat;
    }

    public void setBindWechat(int bindWechat) {
        this.bindWechat = bindWechat;
    }

    public int getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(int bindMobile) {
        this.bindMobile = bindMobile;
    }

    public int getShowNew() {
        return showNew;
    }

    public void setShowNew(int showNew) {
        this.showNew = showNew;
    }
}

