package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.bean.WalletInfoRet;
import com.ydys.elsbballs.model.MyWalletInfoModelImp;
import com.ydys.elsbballs.view.MyWalletInfoView;

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
