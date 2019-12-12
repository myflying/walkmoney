package com.ydys.moneywalk.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ydys.moneywalk.api.UserStepInfoService;
import com.ydys.moneywalk.base.BaseModel;
import com.ydys.moneywalk.base.IBaseRequestCallBack;
import com.ydys.moneywalk.bean.UserStepInfo;
import com.ydys.moneywalk.bean.UserStepInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by admin on 2017/4/7.
 */

public class UserStepInfoModelImp extends BaseModel implements UserStepInfoModel<UserStepInfoRet> {

    private Context context;
    private UserStepInfoService userStepInfoService;
    private CompositeSubscription mCompositeSubscription;

    public UserStepInfoModelImp(Context context) {
        this.context = context;
        userStepInfoService = mRetrofit.create(UserStepInfoService.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void updateStepInfo(UserStepInfo userStepInfo, IBaseRequestCallBack<UserStepInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_id", userStepInfo.getUserId());
            params.put("kilometre", userStepInfo.getKilometre() + "");
            params.put("minute", userStepInfo.getMinute() + "");
            params.put("calorie", userStepInfo.getCalorie() + "");
            params.put("step_num", userStepInfo.getStepNum() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());

        mCompositeSubscription.add(userStepInfoService.updateStepInfo(requestBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<UserStepInfoRet>() {
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
                    public void onNext(UserStepInfoRet userStepInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(userStepInfoRet);
                    }
                }));
    }

}
