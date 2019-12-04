package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.BodyInfoRet;
import com.ydys.moneywalk.bean.CashInitInfoRet;
import com.ydys.moneywalk.model.BodyInfoModelImp;
import com.ydys.moneywalk.model.CashInitInfoModelImp;
import com.ydys.moneywalk.view.BodyInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class CashInitInfoPresenterImp extends BasePresenterImp<IBaseView, CashInitInfoRet> implements CashInitInfoPresenter {

    private Context context = null;
    private CashInitInfoModelImp cashInitInfoModelImp = null;

    public CashInitInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.cashInitInfoModelImp = new CashInitInfoModelImp(context);
    }

    @Override
    public void cashInitMoney(String userId) {
        cashInitInfoModelImp.cashInitMoney(userId, this);
    }
}
