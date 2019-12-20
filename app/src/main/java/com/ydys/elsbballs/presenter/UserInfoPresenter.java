package com.ydys.elsbballs.presenter;

/**
 * Created by admin on 2017/4/7.
 */

public interface UserInfoPresenter {
    void login(String imei, String type, String value, String code,String nickname,String face);

    void imeiLogin(String imei, String agentId, String siteId,int stepNum);

    void validatePhone(String imei, String mobile,String code);
}
