package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.UserInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface UserInfoService {

    @POST("v1.user/login")
    Observable<UserInfoRet> login(@Body RequestBody requestBody);
}
