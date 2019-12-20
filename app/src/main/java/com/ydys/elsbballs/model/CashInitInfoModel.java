package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface CashInitInfoModel<T> {
    void cashInitMoney(String userId, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
