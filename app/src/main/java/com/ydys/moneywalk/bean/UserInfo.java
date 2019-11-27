package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by myflying on 2019/1/25.
 */
public class UserInfo {
    private String id;

    @SerializedName("id_num")
    private String userId;

    private String nickname;//用户昵称
    private String face;//用户头像
    private int gold;//用户金币
    private double amount;//用户金币对应金额
    @SerializedName("has_reg")
    private int hasReg;//用户是否之前已经注册 1是 0否

    private String openid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getHasReg() {
        return hasReg;
    }

    public void setHasReg(int hasReg) {
        this.hasReg = hasReg;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
