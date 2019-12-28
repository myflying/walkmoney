package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.CashMoneyRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface CashMoneyService {

    @POST("v2.cash_out/doCashOut")
    Observable<CashMoneyRet> cashMoney(@Body RequestBody requestBody);

}
