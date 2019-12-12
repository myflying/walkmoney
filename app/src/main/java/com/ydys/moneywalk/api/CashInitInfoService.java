package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.CashInitInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface CashInitInfoService {

    @POST("v1.cash_out/cashOutPage")
    Observable<CashInitInfoRet> cashInitMoney(@Body RequestBody requestBody);

}
