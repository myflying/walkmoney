package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface SignInfoModel<T> {
    void signDay(String userId, String day, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
