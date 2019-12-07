package com.ydys.moneywalk.model;


import com.ydys.moneywalk.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface CashMoneyModel<T> {
    void cashMoney(String userId, String money, int isNewPer, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
