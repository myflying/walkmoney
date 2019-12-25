package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.UserInfoRet;
import com.ydys.elsbballs.model.UserInfoModelImp;

/**
 * Created by admin on 2017/4/7.
 */

public class UserInfoPresenterImp extends BasePresenterImp<IBaseView, UserInfoRet> implements UserInfoPresenter {

    private Context context = null;
    private UserInfoModelImp userInfoModelImp = null;

    public UserInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.userInfoModelImp = new UserInfoModelImp(context);
    }

    @Override
    public void login(String imei, String type, String value, String code, String nickname, String face) {
        userInfoModelImp.login(imei, type, value, code, nickname, face, this);
    }

    @Override
    public void imeiLogin(String imei, String agentId, String softId, String softName) {
        userInfoModelImp.imeiLogin(imei, agentId, softId, softName, this);
    }

    @Override
    public void validatePhone(String imei, String mobile, String code) {
        userInfoModelImp.validatePhone(imei, mobile, code, this);
    }
}
