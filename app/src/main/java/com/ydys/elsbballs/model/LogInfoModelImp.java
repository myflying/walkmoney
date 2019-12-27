package com.ydys.elsbballs.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ydys.elsbballs.api.BodyInfoService;
import com.ydys.elsbballs.api.LogInfoService;
import com.ydys.elsbballs.base.BaseModel;
import com.ydys.elsbballs.base.IBaseRequestCallBack;
import com.ydys.elsbballs.bean.BodyInfoRet;
import com.ydys.elsbballs.bean.LogInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by admin on 2017/4/7.
 */

public class LogInfoModelImp extends BaseModel implements LogInfoModel<LogInfoRet> {

    private Context context;
    private LogInfoService logInfoService;
    private CompositeSubscription mCompositeSubscription;

    public LogInfoModelImp(Context context) {
        this.context = context;
        logInfoService = mRetrofit.create(LogInfoService.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void addLogInfo(String userId, String adId, String adCode, String pos, String type, IBaseRequestCallBack<LogInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", userId);
            params.put("ad_id", adId);
            params.put("ad_code", adCode);
            params.put("position", pos);
            params.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(logInfoService.addLogInfo(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<LogInfoRet>() {
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
                    public void onNext(LogInfoRet logInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(logInfoRet);
                    }
                }));
    }

}
