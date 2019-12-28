package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.BodyInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface BodyInfoService {

    @POST("v2.user/getBodyData")
    Observable<BodyInfoRet> getBodyInfo(@Body RequestBody requestBody);

    @POST("v2.user/fillBodyData")
    Observable<BodyInfoRet> updateBodyInfo(@Body RequestBody requestBody);

}
