package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.ShareInfoRet;
import com.ydys.moneywalk.model.ShareInfoModelImp;
import com.ydys.moneywalk.view.ShareInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class ShareInfoPresenterImp extends BasePresenterImp<ShareInfoView, ShareInfoRet> implements ShareInfoPresenter {

    private Context context = null;
    private ShareInfoModelImp shareInfoModelImp = null;

    public ShareInfoPresenterImp(ShareInfoView view, Context context) {
        super(view);
        this.context = context;
        this.shareInfoModelImp = new ShareInfoModelImp(context);
    }

    @Override
    public void shareDone(String userId, int type) {
        shareInfoModelImp.shareDone(userId, type, this);
    }
}
