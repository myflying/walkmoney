package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.ReportInfoRet;
import com.ydys.elsbballs.model.ReportInfoModelImp;

/**
 * Created by admin on 2017/4/7.
 */

public class ReportInfoPresenterImp extends BasePresenterImp<IBaseView, ReportInfoRet> implements ReportInfoPresenter {

    private Context context = null;
    private ReportInfoModelImp reportInfoModelImp = null;

    public ReportInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.reportInfoModelImp = new ReportInfoModelImp(context);
    }

    @Override
    public void startPlayGame(String userId) {
        reportInfoModelImp.startPlayGame(userId, this);
    }
}
