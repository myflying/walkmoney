package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.bean.WalletInfoRet;
import com.ydys.moneywalk.model.MyWalletInfoModelImp;
import com.ydys.moneywalk.view.MyWalletInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class MyWalletInfoPresenterImp extends BasePresenterImp<MyWalletInfoView, WalletInfoRet> implements MyWalletInfoPresenter {

    private Context context = null;
    private MyWalletInfoModelImp myWalletInfoModelImp = null;

    public MyWalletInfoPresenterImp(MyWalletInfoView view, Context context) {
        super(view);
        this.context = context;
        this.myWalletInfoModelImp = new MyWalletInfoModelImp(context);
    }

    @Override
    public void loadWalletInfo(String userId) {
        myWalletInfoModelImp.loadWalletInfo(userId, this);
    }
}
