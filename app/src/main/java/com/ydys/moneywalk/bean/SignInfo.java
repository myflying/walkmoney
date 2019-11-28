package com.ydys.moneywalk.bean;

import com.google.gson.annotations.SerializedName;

public class SignInfo {

    private int gold;//金币
    @SerializedName("is_sign")
    private int isSign;//是否已经签到 1是 0否

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getIsSign() {
        return isSign;
    }

    public void setIsSign(int isSign) {
        this.isSign = isSign;
    }
}
