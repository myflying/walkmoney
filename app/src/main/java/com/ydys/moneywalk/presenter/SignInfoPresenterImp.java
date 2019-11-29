package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.SendMsgInfoRet;
import com.ydys.moneywalk.bean.SignInfoRet;
import com.ydys.moneywalk.model.SendMsgInfoModelImp;
import com.ydys.moneywalk.model.SignInfoModelImp;

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
    public void signDay(String userId, int isDouble, int day) {
        signInfoModelImp.signDay(userId, isDouble, day, this);
    }
}
