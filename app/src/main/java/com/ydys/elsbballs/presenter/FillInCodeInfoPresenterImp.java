package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.bean.FillInCodeInfoRet;
import com.ydys.elsbballs.model.FillInCodeInfoModelImp;
import com.ydys.elsbballs.view.FillInCodeView;

/**
 * Created by admin on 2017/4/7.
 */

public class FillInCodeInfoPresenterImp extends BasePresenterImp<FillInCodeView, FillInCodeInfoRet> implements FillInCodeInfoPresenter {

    private Context context = null;
    private FillInCodeInfoModelImp fillInCodeInfoModelImp = null;

    public FillInCodeInfoPresenterImp(FillInCodeView view, Context context) {
        super(view);
        this.context = context;
        this.fillInCodeInfoModelImp = new FillInCodeInfoModelImp(context);
    }

    @Override
    public void fillInCode(String userId, String fromUserId) {
        fillInCodeInfoModelImp.fillInCode(userId, fromUserId, this);
    }
}
