package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.GameTimeInfoRet;
import com.ydys.elsbballs.model.GameTimeInfoModelImp;

/**
 * Created by admin on 2017/4/7.
 */

public class GameTimeInfoPresenterImp extends BasePresenterImp<IBaseView, GameTimeInfoRet> implements GameTimeInfoPresenter {

    private Context context = null;
    private GameTimeInfoModelImp gameTimeInfoModelImp = null;

    public GameTimeInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.gameTimeInfoModelImp = new GameTimeInfoModelImp(context);
    }

    @Override
    public void upGameTime(String userId) {
        gameTimeInfoModelImp.upGameTime(userId, this);
    }
}
