package com.ydys.moneywalk.bean;

import java.util.List;

/**
 * Created by myflying on 2018/11/16.
 */
public class WalletInfoRet extends ResultInfo {

    private MyWalletWapper data;

    public MyWalletWapper getData() {
        return data;
    }

    public void setData(MyWalletWapper data) {
        this.data = data;
    }
}
