package com.ydys.moneywalk.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/16.
 */
public class BodyInfoRet extends ResultInfo {

    private List<BodyInfo> data;

    public List<BodyInfo> getData() {
        return data;
    }

    public void setData(List<BodyInfo> data) {
        this.data = data;
    }
}
