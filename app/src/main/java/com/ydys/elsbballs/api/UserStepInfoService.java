package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.UserStepInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface UserStepInfoService {

    @POST("v2.User/updActivityData")
    Observable<UserStepInfoRet> updateStepInfo(@Body RequestBody requestBody);

}
