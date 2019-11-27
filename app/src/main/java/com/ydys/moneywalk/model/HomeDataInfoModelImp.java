package com.ydys.moneywalk.model;

import android.content.Context;

import com.ydys.moneywalk.api.HomeDataInfoService;
import com.ydys.moneywalk.base.BaseModel;
import com.ydys.moneywalk.base.IBaseRequestCallBack;
import com.ydys.moneywalk.bean.HomeDateInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by admin on 2017/4/7.
 */

public class HomeDataInfoModelImp extends BaseModel implements HomeDataInfoModel<HomeDateInfoRet> {

    private Context context;
    private HomeDataInfoService homeDataInfoService;
    private CompositeSubscription mCompositeSubscription;

    public HomeDataInfoModelImp(Context context) {
        this.context = context;
        homeDataInfoService = mRetrofit.create(HomeDataInfoService.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void initHomeData(IBaseRequestCallBack<HomeDateInfoRet> iBaseRequestCallBack) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "");
        mCompositeSubscription.add(homeDataInfoService.initHomeData(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<HomeDateInfoRet>() {
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
                    public void onNext(HomeDateInfoRet homeDateInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(homeDateInfoRet);
                    }
                }));
    }
}
