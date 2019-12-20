package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.CashRecordInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface CashRecordInfoService {

    @POST("v1.cash_out/getLog")
    Observable<CashRecordInfoRet> cashRecordList(@Body RequestBody requestBody);

}
