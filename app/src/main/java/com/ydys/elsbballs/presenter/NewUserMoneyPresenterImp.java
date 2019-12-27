package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.BodyInfoRet;
import com.ydys.elsbballs.bean.NewUserMoneyRet;
import com.ydys.elsbballs.model.BodyInfoModelImp;
import com.ydys.elsbballs.model.NewUserMoneyModelImp;
import com.ydys.elsbballs.view.BodyInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class NewUserMoneyPresenterImp extends BasePresenterImp<IBaseView, NewUserMoneyRet> implements NewUserMoneyPresenter {

    private Context context = null;
    private NewUserMoneyModelImp newUserMoneyModelImp = null;

    public NewUserMoneyPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.newUserMoneyModelImp = new NewUserMoneyModelImp(context);
    }

    @Override
    public void newUserMoney(String userId) {
        newUserMoneyModelImp.newUserMoney(userId, this);
    }
}
