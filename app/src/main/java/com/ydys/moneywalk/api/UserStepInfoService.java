package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.ResultInfo;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.bean.UserStepInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface UserStepInfoService {

    @POST("v1.User/updActivityData")
    Observable<UserStepInfoRet> updateStepInfo(@Body RequestBody requestBody);

}
