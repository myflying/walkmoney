package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.BodyInfoRet;
import com.ydys.moneywalk.bean.SignInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface BodyInfoService {

    @POST("v1.User/fillBodyData")
    Observable<BodyInfoRet> updateBodyInfo(@Body RequestBody requestBody);

}
