package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.SendMsgInfoRet;
import com.ydys.moneywalk.bean.ShareInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface ShareInfoService {

    @POST("v1.Task/shareSuccess")
    Observable<ShareInfoRet> shareDone(@Body RequestBody requestBody);

}
