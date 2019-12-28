package com.ydys.elsbballs.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ydys.elsbballs.api.VersionInfoService;
import com.ydys.elsbballs.base.BaseModel;
import com.ydys.elsbballs.base.IBaseRequestCallBack;
import com.ydys.elsbballs.bean.VersionInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by admin on 2017/4/7.
 */

public class VersionInfoModelImp extends BaseModel implements VersionInfoModel<VersionInfoRet> {

    private Context context;
    private VersionInfoService versionInfoService;
    private CompositeSubscription mCompositeSubscription;

    public VersionInfoModelImp(Context context) {
        this.context = context;
        versionInfoService = mRetrofit.create(VersionInfoService.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void updateVersion(String channel,IBaseRequestCallBack<VersionInfoRet> iBaseRequestCallBack) {

        JSONObject params = new JSONObject();
        try {
            params.put("agent_id", channel);
            params.put("app_id","17");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(versionInfoService.versionUpdate(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<VersionInfoRet>() {
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
                    public void onNext(VersionInfoRet versionInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(versionInfoRet);
                    }
                }));
    }
}
