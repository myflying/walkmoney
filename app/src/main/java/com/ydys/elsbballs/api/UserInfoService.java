package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.FillInCodeInfoRet;
import com.ydys.elsbballs.bean.UserInfoRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface UserInfoService {

    @POST("v1.User/bindOrLogin")
    Observable<UserInfoRet> login(@Body RequestBody requestBody);


    @POST("v1.user/imeiLogin")
    Observable<UserInfoRet> imeiLogin(@Body RequestBody requestBody);


    @POST("v1.User/fillInCode")
    Observable<FillInCodeInfoRet> fillInCode(@Body RequestBody requestBody);


    @POST("v1.cash_out/checkMobile")
    Observable<UserInfoRet> validatePhone(@Body RequestBody requestBody);
}
