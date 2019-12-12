package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.WalletInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface MyWalletInfoService {

    @POST("v1.user/myWallet")
    Observable<WalletInfoRet> loadWalletInfo(@Body RequestBody requestBody);

}
