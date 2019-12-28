package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.WalletInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface MyWalletInfoService {

    @POST("v2.user/myWallet")
    Observable<WalletInfoRet> loadWalletInfo(@Body RequestBody requestBody);

}
