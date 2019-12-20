package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface TaskInfoModel<T> {
    void taskList(String userId, int isLogin,IBaseRequestCallBack<T> iBaseRequestCallBack);
}
