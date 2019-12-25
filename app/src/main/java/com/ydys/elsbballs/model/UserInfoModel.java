package com.ydys.elsbballs.model;


import com.ydys.elsbballs.base.IBaseRequestCallBack;

/**
 * Created by admin on 2017/4/7.
 */

public interface UserInfoModel<T> {
    void login(String imei, String type, String value, String code, String nickname, String face, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void imeiLogin(String imei, String agentId, String softId,String softName, IBaseRequestCallBack<T> iBaseRequestCallBack);

    void validatePhone(String imei, String mobile, String code, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
