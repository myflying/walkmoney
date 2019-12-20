package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.SignInfoRet;
import com.ydys.elsbballs.model.SignInfoModelImp;

/**
 * Created by admin on 2017/4/7.
 */

public class SignInfoPresenterImp extends BasePresenterImp<IBaseView, SignInfoRet> implements SignInfoPresenter {

    private Context context = null;
    private SignInfoModelImp signInfoModelImp = null;

    public SignInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.signInfoModelImp = new SignInfoModelImp(context);
    }

    @Override
    public void signDay(String userId, String day) {
        signInfoModelImp.signDay(userId, day, this);
    }
}
