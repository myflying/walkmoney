package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.BodyInfoRet;
import com.ydys.elsbballs.bean.LogInfoRet;
import com.ydys.elsbballs.model.BodyInfoModelImp;
import com.ydys.elsbballs.model.LogInfoModelImp;
import com.ydys.elsbballs.view.BodyInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class LogInfoPresenterImp extends BasePresenterImp<IBaseView, LogInfoRet> implements LogInfoPresenter {

    private Context context = null;
    private LogInfoModelImp logInfoModelImp = null;

    public LogInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.logInfoModelImp = new LogInfoModelImp(context);
    }

    @Override
    public void addLogInfo(String userId, String adId, String adCode, String pos, String type) {
        logInfoModelImp.addLogInfo(userId, adId, adCode, pos, type, this);
    }
}
