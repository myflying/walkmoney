package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.bean.CashRecordInfoRet;
import com.ydys.elsbballs.model.CashRecordInfoModelImp;
import com.ydys.elsbballs.view.CashRecordInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class CashRecordInfoPresenterImp extends BasePresenterImp<CashRecordInfoView, CashRecordInfoRet> implements CashRecordInfoPresenter {

    private Context context = null;
    private CashRecordInfoModelImp cashRecordInfoModelImp = null;

    public CashRecordInfoPresenterImp(CashRecordInfoView view, Context context) {
        super(view);
        this.context = context;
        this.cashRecordInfoModelImp = new CashRecordInfoModelImp(context);
    }

    @Override
    public void cashRecordList(String userId, int page) {
        cashRecordInfoModelImp.cashRecordList(userId, page, this);
    }
}
