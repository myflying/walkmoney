package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.CashInitInfoRet;
import com.ydys.elsbballs.model.CashInitInfoModelImp;

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
