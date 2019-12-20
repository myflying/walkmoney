package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.HomeDateInfoRet;
import com.ydys.elsbballs.model.HomeDataInfoModelImp;

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
