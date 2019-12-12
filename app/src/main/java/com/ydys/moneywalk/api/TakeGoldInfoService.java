package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.TakeGoldInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface TakeGoldInfoService {

    @POST("v1.Home/collectGoldLucky")
    Observable<TakeGoldInfoRet> takeLuckGold(@Body RequestBody requestBody);

    @POST("v1.Home/collectGoldStepNum")
    Observable<TakeGoldInfoRet> takeStepGold(@Body RequestBody requestBody);

    @POST("v1.Home/collectGoldStage")
    Observable<TakeGoldInfoRet> takeStageGold(@Body RequestBody requestBody);

    @POST("v1.Task/receiveTaskGold")
    Observable<TakeGoldInfoRet> takeTaskGold(@Body RequestBody requestBody);
}
