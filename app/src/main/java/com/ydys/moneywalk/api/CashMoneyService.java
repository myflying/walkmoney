package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.CashInitInfoRet;
import com.ydys.moneywalk.bean.CashMoneyRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface CashMoneyService {

    @POST("v1.cash_out/doCashOut")
    Observable<CashMoneyRet> cashMoney(@Body RequestBody requestBody);

}
