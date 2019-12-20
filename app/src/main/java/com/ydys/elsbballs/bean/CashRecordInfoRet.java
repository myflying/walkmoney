package com.ydys.elsbballs.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/16.
 */
public class CashRecordInfoRet extends ResultInfo {

    private List<CashRecordInfo> data;

    public List<CashRecordInfo> getData() {
        return data;
    }

    public void setData(List<CashRecordInfo> data) {
        this.data = data;
    }
}
