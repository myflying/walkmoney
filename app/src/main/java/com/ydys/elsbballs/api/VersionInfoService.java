package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.VersionInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface VersionInfoService {

    @POST("v2.common/version")
    Observable<VersionInfoRet> versionUpdate(@Body RequestBody requestBody);

}
