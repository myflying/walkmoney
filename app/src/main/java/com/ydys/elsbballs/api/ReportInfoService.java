package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.BodyInfoRet;
import com.ydys.elsbballs.bean.ReportInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface ReportInfoService {

    @POST("v1.Home/startPlay")
    Observable<ReportInfoRet> startPlayGame(@Body RequestBody requestBody);

}
