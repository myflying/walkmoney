package com.ydys.moneywalk.presenter;

/**
 * Created by admin on 2017/4/7.
 */

public interface UserInfoPresenter {
    void login(String imei, String type, String value, String code,String nickname,String face);

    void imeiLogin(String imei, String agentId, String siteId);

    void validatePhone(String imei, String mobile,String code);
}
