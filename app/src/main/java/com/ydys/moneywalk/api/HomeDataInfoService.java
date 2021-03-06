package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.HomeDateInfoRet;
import com.ydys.moneywalk.bean.UserInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface HomeDataInfoService {

    @POST("v1.home/index")
    Observable<HomeDateInfoRet> initHomeData(@Body RequestBody requestBody);
}
