package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.SendMsgInfoRet;
import com.ydys.moneywalk.model.SendMsgInfoModelImp;
import com.ydys.moneywalk.view.SendMsgInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class SendMsgInfoPresenterImp extends BasePresenterImp<IBaseView, SendMsgInfoRet> implements SendMsgInfoPresenter {

    private Context context = null;
    private SendMsgInfoModelImp sendMsgInfoModelImp = null;

    public SendMsgInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.sendMsgInfoModelImp = new SendMsgInfoModelImp(context);
    }

    @Override
    public void sendMsg(String phoneNumber) {
        sendMsgInfoModelImp.sendMsg(phoneNumber, this);
    }
}
