package com.ydys.moneywalk.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/16.
 */
public class SendMsgInfoRet extends ResultInfo {

    private List<SendMsgInfo> data;

    public List<SendMsgInfo> getData() {
        return data;
    }

    public void setData(List<SendMsgInfo> data) {
        this.data = data;
    }
}
