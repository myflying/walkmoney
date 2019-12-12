package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.bean.FillInCodeInfoRet;
import com.ydys.moneywalk.model.FillInCodeInfoModelImp;
import com.ydys.moneywalk.view.FillInCodeView;

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
