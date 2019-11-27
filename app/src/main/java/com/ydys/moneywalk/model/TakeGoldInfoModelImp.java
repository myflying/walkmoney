package com.ydys.moneywalk.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ydys.moneywalk.api.SendMsgInfoService;
import com.ydys.moneywalk.api.TakeGoldInfoService;
import com.ydys.moneywalk.base.BaseModel;
import com.ydys.moneywalk.base.IBaseRequestCallBack;
import com.ydys.moneywalk.bean.SendMsgInfoRet;
import com.ydys.moneywalk.bean.TakeGoldInfo;
import com.ydys.moneywalk.bean.TakeGoldInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by admin on 2017/4/7.
 */

public class TakeGoldInfoModelImp extends BaseModel implements TakeGoldInfoModel<TakeGoldInfoRet> {

    private Context context;
    private TakeGoldInfoService takeGoldInfoService;
    private CompositeSubscription mCompositeSubscription;

    public TakeGoldInfoModelImp(Context context) {
        this.context = context;
        takeGoldInfoService = mRetrofit.create(TakeGoldInfoService.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void takeLuckGold(TakeGoldInfo takeGoldInfo, IBaseRequestCallBack<TakeGoldInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", takeGoldInfo.getUserId());
            params.put("task_id", takeGoldInfo.getTaskId());
            params.put("gold", takeGoldInfo.getGold() + "");
            params.put("is_double", takeGoldInfo.getIsDouble() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(takeGoldInfoService.takeLuckGold(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TakeGoldInfoRet>() {
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
                    public void onNext(TakeGoldInfoRet takeGoldInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(takeGoldInfoRet);
                    }
                }));
    }

    @Override
    public void takeStepGold(TakeGoldInfo takeGoldInfo, IBaseRequestCallBack<TakeGoldInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", takeGoldInfo.getUserId());
            params.put("task_id", takeGoldInfo.getTaskId());
            params.put("gold", takeGoldInfo.getGold() + "");
            params.put("is_play", takeGoldInfo.getIsPlay() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(takeGoldInfoService.takeLuckGold(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TakeGoldInfoRet>() {
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
                    public void onNext(TakeGoldInfoRet takeGoldInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(takeGoldInfoRet);
                    }
                }));
    }

    @Override
    public void takeStageGold(TakeGoldInfo takeGoldInfo, IBaseRequestCallBack<TakeGoldInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", takeGoldInfo.getUserId());
            params.put("task_id", takeGoldInfo.getTaskId());
            params.put("gold", takeGoldInfo.getGold() + "");
            params.put("stage", takeGoldInfo.getStage() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(takeGoldInfoService.takeLuckGold(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<TakeGoldInfoRet>() {
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
                    public void onNext(TakeGoldInfoRet takeGoldInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(takeGoldInfoRet);
                    }
                }));
    }

}
