package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.CashMoneyRet;
import com.ydys.moneywalk.model.CashMoneyModelImp;

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
