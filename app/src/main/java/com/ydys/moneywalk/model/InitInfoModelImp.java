package com.ydys.moneywalk.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ydys.moneywalk.api.InitInfoService;
import com.ydys.moneywalk.base.BaseModel;
import com.ydys.moneywalk.base.IBaseRequestCallBack;
import com.ydys.moneywalk.bean.InitInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by admin on 2017/4/7.
 */

public class InitInfoModelImp extends BaseModel implements InitInfoModel<InitInfoRet> {

    private Context context;
    private InitInfoService initInfoService;
    private CompositeSubscription mCompositeSubscription;

    public InitInfoModelImp(Context context) {
        this.context = context;
        initInfoService = mRetrofit.create(InitInfoService.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void initInfo(String userId,IBaseRequestCallBack<InitInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());
        mCompositeSubscription.add(initInfoService.initInfo(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<InitInfoRet>() {
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
                    public void onNext(InitInfoRet initInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(initInfoRet);
                    }
                }));
    }

}
