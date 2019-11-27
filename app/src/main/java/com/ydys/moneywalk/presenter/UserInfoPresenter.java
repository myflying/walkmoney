package com.ydys.moneywalk.presenter;

/**
 * Created by admin on 2017/4/7.
 */

public interface UserInfoPresenter {
    void login(String userId, String userName);
    void imeiLogin(String imei, String agentId, String siteId);
}
