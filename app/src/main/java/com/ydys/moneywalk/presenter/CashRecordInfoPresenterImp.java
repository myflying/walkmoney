package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.bean.CashRecordInfoRet;
import com.ydys.moneywalk.bean.FillInCodeInfoRet;
import com.ydys.moneywalk.model.CashRecordInfoModelImp;
import com.ydys.moneywalk.model.FillInCodeInfoModelImp;
import com.ydys.moneywalk.view.CashRecordInfoView;
import com.ydys.moneywalk.view.FillInCodeView;

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
