package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.bean.BodyInfoRet;
import com.ydys.elsbballs.model.BodyInfoModelImp;
import com.ydys.elsbballs.view.BodyInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class BodyInfoPresenterImp extends BasePresenterImp<BodyInfoView, BodyInfoRet> implements BodyInfoPresenter {

    private Context context = null;
    private BodyInfoModelImp bodyInfoModelImp = null;

    public BodyInfoPresenterImp(BodyInfoView view, Context context) {
        super(view);
        this.context = context;
        this.bodyInfoModelImp = new BodyInfoModelImp(context);
    }

    @Override
    public void getBodyInfo(String userId) {
        bodyInfoModelImp.getBodyInfo(userId, this);
    }

    @Override
    public void updateBodyInfo(String userId, String filed, String value) {
        bodyInfoModelImp.updateBodyInfo(userId, filed, value, this);
    }
}
