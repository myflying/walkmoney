package com.ydys.moneywalk.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/16.
 */
public class FillInCodeInfoRet extends ResultInfo {

    private List<FillInCodeInfo> data;

    public List<FillInCodeInfo> getData() {
        return data;
    }

    public void setData(List<FillInCodeInfo> data) {
        this.data = data;
    }
}
