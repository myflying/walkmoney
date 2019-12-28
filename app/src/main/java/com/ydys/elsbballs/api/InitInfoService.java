package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.InitInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface InitInfoService {

    @POST("v2.home/init")
    Observable<InitInfoRet> initInfo(@Body RequestBody requestBody);
}
