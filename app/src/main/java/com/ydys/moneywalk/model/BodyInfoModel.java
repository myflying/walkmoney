package com.ydys.moneywalk.model;


import com.ydys.moneywalk.base.IBaseRequestCallBack;
import com.ydys.moneywalk.bean.BodyInfo;

/**
 * Created by admin on 2017/4/7.
 */

public interface BodyInfoModel<T> {
    void updateBodyInfo(String userId, String filed, String value, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
