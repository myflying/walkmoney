package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.SendMsgInfoRet;
import com.ydys.elsbballs.model.SendMsgInfoModelImp;

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

    @Override
    public void sendValidateMsg(String phoneNumber) {
        sendMsgInfoModelImp.sendValidateMsg(phoneNumber, this);
    }
}
