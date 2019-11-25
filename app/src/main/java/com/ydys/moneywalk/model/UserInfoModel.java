package com.ydys.moneywalk.model;


import com.ydys.moneywalk.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface UserInfoModel<T> {
    void login(String userId,String userName, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
