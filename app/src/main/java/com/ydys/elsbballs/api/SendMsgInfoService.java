package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.SendMsgInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface SendMsgInfoService {

    @POST("v1.Common/sendBindSms")
    Observable<SendMsgInfoRet> sendMsg(@Body RequestBody requestBody);


    @POST("v1.Common/sendCashOutSms")
    Observable<SendMsgInfoRet> sendValidatePhone(@Body RequestBody requestBody);
}
