package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface NewUserMoneyModel<T> {
    void newUserMoney(String userId, IBaseRequestCallBack<T> iBaseRequestCallBack);
}