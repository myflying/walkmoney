package com.ydys.moneywalk.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/16.
 */
public class TakeGoldInfoRet extends ResultInfo {

    private List<TakeGoldInfo> data;

    public List<TakeGoldInfo> getData() {
        return data;
    }

    public void setData(List<TakeGoldInfo> data) {
        this.data = data;
    }
}
