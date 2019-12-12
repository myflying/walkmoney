package com.ydys.moneywalk.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ydys.moneywalk.api.CashInitInfoService;
import com.ydys.moneywalk.base.BaseModel;
import com.ydys.moneywalk.base.IBaseRequestCallBack;
import com.ydys.moneywalk.bean.CashInitInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by admin on 2017/4/7.
 */

public class CashInitInfoModelImp extends BaseModel implements CashInitInfoModel<CashInitInfoRet> {

    private Context context;
    private CashInitInfoService cashInitInfoService;
    private CompositeSubscription mCompositeSubscription;

    public CashInitInfoModelImp(Context context) {
        this.context = context;
        cashInitInfoService = mRetrofit.create(CashInitInfoService.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void cashInitMoney(String userId, IBaseRequestCallBack<CashInitInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(cashInitInfoService.cashInitMoney(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<CashInitInfoRet>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onCompleted() {
                        //回调接口：请求已完成，可以隐藏progress
                        iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //回调接口：请求异常
                        iBaseRequestCallBack.requestError(e);
                    }

                    @Override
                    public void onNext(CashInitInfoRet cashInitInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(cashInitInfoRet);
                    }
                }));
    }
}
