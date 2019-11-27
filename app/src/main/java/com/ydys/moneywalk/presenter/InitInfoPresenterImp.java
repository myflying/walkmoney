package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.InitInfoRet;
import com.ydys.moneywalk.model.InitInfoModelImp;
import com.ydys.moneywalk.view.InitInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class InitInfoPresenterImp extends BasePresenterImp<IBaseView, InitInfoRet> implements InitInfoPresenter {

    private Context context = null;
    private InitInfoModelImp initInfoModelImp = null;

    public InitInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.initInfoModelImp = new InitInfoModelImp(context);
    }

    @Override
    public void initInfo(String userId) {
        initInfoModelImp.initInfo(userId, this);
    }
}
