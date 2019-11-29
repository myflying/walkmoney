package com.ydys.moneywalk.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/16.
 */
public class SignInfoRet extends ResultInfo {

    private List<SignInfo> data;

    public List<SignInfo> getData() {
        return data;
    }

    public void setData(List<SignInfo> data) {
        this.data = data;
    }
}
