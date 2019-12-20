package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface BodyInfoModel<T> {
    void getBodyInfo(String userId,IBaseRequestCallBack<T> iBaseRequestCallBack);

    void updateBodyInfo(String userId, String filed, String value, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
