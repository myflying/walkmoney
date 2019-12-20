package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface SendMsgInfoModel<T> {
    void sendMsg(String phoneNumber, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void sendValidateMsg(String phoneNumber, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
