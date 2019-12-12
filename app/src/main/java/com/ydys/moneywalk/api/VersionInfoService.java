package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.VersionInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface VersionInfoService {

    @POST("v1.common/version")
    Observable<VersionInfoRet> versionUpdate(@Body RequestBody requestBody);

}
