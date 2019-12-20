package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.VersionInfoRet;
import com.ydys.elsbballs.model.VersionInfoModelImp;

/**
 * Created by admin on 2017/4/7.
 */

public class VersionInfoPresenterImp extends BasePresenterImp<IBaseView, VersionInfoRet> implements VersionInfoPresenter {

    private Context context = null;
    private VersionInfoModelImp versionInfoModelImp = null;

    public VersionInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.versionInfoModelImp = new VersionInfoModelImp(context);
    }

    @Override
    public void updateVersion(String channel) {
        versionInfoModelImp.updateVersion(channel,this);
    }
}
