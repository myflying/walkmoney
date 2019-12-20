package com.ydys.elsbballs.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoldDayInfo {
    @SerializedName("rq")
    private String goldDate;

    @SerializedName("list")
    private List<GoldDetailInfo> detailList;

    public String getGoldDate() {
        return goldDate;
    }

    public void setGoldDate(String goldDate) {
        this.goldDate = goldDate;
    }

    public List<GoldDetailInfo> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<GoldDetailInfo> detailList) {
        this.detailList = detailList;
    }
}
