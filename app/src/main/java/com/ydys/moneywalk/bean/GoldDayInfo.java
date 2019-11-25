package com.ydys.moneywalk.bean;

import java.util.List;

public class GoldDayInfo {
    private String goldDate;
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
