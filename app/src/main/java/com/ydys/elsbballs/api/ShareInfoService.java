package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.ShareInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface ShareInfoService {

    @POST("v2.Task/shareSuccess")
    Observable<ShareInfoRet> shareDone(@Body RequestBody requestBody);

}
