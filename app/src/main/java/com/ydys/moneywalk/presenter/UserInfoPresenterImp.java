package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.model.UserInfoModelImp;
import com.ydys.moneywalk.view.UserInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class UserInfoPresenterImp extends BasePresenterImp<UserInfoView, UserInfoRet> implements UserInfoPresenter {

    private Context context = null;
    private UserInfoModelImp userInfoModelImp = null;

    public UserInfoPresenterImp(UserInfoView view, Context context) {
        super(view);
        this.context = context;
        this.userInfoModelImp = new UserInfoModelImp(context);
    }

    @Override
    public void login(String userId, String userName) {
        userInfoModelImp.login(userId, userName, this);
    }
}
