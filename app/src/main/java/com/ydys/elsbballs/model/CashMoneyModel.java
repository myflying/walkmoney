package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface CashMoneyModel<T> {
    void cashMoney(String userId, String money, int isNewPer, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
