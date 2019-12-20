package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.UserStepInfo;
import com.ydys.elsbballs.bean.UserStepInfoRet;
import com.ydys.elsbballs.model.UserStepInfoModelImp;

/**
 * Created by admin on 2017/4/7.
 */

public class UserStepInfoPresenterImp extends BasePresenterImp<IBaseView, UserStepInfoRet> implements UserStepInfoPresenter {

    private Context context = null;
    private UserStepInfoModelImp userStepInfoModelImp = null;

    public UserStepInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.userStepInfoModelImp = new UserStepInfoModelImp(context);
    }

    @Override
    public void updateStepInfo(UserStepInfo userStepInfo) {
        userStepInfoModelImp.updateStepInfo(userStepInfo, this);
    }
}
