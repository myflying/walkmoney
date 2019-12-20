package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.CashMoneyRet;
import com.ydys.elsbballs.model.CashMoneyModelImp;

/**
 * Created by admin on 2017/4/7.
 */

public class CashMoneyPresenterImp extends BasePresenterImp<IBaseView, CashMoneyRet> implements CashMoneyPresenter {

    private Context context = null;
    private CashMoneyModelImp cashMoneyModelImp = null;

    public CashMoneyPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.cashMoneyModelImp = new CashMoneyModelImp(context);
    }

    @Override
    public void cashMoney(String userId, String money, int isNewPer) {
        cashMoneyModelImp.cashMoney(userId, money, isNewPer, this);
    }
}
