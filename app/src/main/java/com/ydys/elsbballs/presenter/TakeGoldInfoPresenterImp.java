package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.TakeGoldInfo;
import com.ydys.elsbballs.bean.TakeGoldInfoRet;
import com.ydys.elsbballs.model.TakeGoldInfoModelImp;

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

    @Override
    public void takeTaskGold(TakeGoldInfo takeGoldInfo) {
        takeGoldInfoModelImp.takeTaskGold(takeGoldInfo, this);
    }
}
