package com.ydys.moneywalk.model;


import com.ydys.moneywalk.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface FillInCodeInfoModel<T> {
    void fillInCode(String userId,String fromUserId, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
