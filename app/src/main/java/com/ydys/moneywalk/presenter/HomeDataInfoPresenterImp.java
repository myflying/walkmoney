package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.HomeDateInfoRet;
import com.ydys.moneywalk.model.HomeDataInfoModelImp;
import com.ydys.moneywalk.view.HomeDataInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class HomeDataInfoPresenterImp extends BasePresenterImp<IBaseView, HomeDateInfoRet> implements HomeDataInfoPresenter {

    private Context context = null;
    private HomeDataInfoModelImp homeDataInfoModelImp = null;

    public HomeDataInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.homeDataInfoModelImp = new HomeDataInfoModelImp(context);
    }

    @Override
    public void initHomeData() {
        homeDataInfoModelImp.initHomeData(this);
    }
}
