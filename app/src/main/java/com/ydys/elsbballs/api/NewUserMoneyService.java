package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.BodyInfoRet;
import com.ydys.elsbballs.bean.NewUserMoneyRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface NewUserMoneyService {

    @POST("v1.Home/getNewUserGold")
    Observable<NewUserMoneyRet> newUserMoney(@Body RequestBody requestBody);

}
