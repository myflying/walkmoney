package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.TakeGoldInfo;
import com.ydys.moneywalk.bean.TakeGoldInfoRet;
import com.ydys.moneywalk.model.TakeGoldInfoModelImp;

/**
 * Created by admin on 2017/4/7.
 */

public class TakeGoldInfoPresenterImp extends BasePresenterImp<IBaseView, TakeGoldInfoRet> implements TakeGoldInfoPresenter {

    private Context context = null;
    private TakeGoldInfoModelImp takeGoldInfoModelImp = null;

    public TakeGoldInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.takeGoldInfoModelImp = new TakeGoldInfoModelImp(context);
    }

    @Override
    public void takeLuckGold(TakeGoldInfo takeGoldInfo) {
        takeGoldInfoModelImp.takeLuckGold(takeGoldInfo, this);
    }

    @Override
    public void takeStepGold(TakeGoldInfo takeGoldInfo) {
        takeGoldInfoModelImp.takeStepGold(takeGoldInfo, this);
    }

    @Override
    public void takeStageGold(TakeGoldInfo takeGoldInfo) {
        takeGoldInfoModelImp.takeStageGold(takeGoldInfo, this);
    }
}
