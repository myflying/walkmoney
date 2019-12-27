package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.BodyInfoRet;
import com.ydys.elsbballs.bean.LogInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface LogInfoService {

    @POST("v1.ad/addLog")
    Observable<LogInfoRet> addLogInfo(@Body RequestBody requestBody);

}
