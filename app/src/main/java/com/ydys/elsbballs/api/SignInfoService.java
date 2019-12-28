package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.SignInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface SignInfoService {

    @POST("v2.Task/doDoubleSign")
    Observable<SignInfoRet> signDay(@Body RequestBody requestBody);

}
