package com.ydys.moneywalk.model;


import com.ydys.moneywalk.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface BodyInfoModel<T> {
    void getBodyInfo(String userId,IBaseRequestCallBack<T> iBaseRequestCallBack);

    void updateBodyInfo(String userId, String filed, String value, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
