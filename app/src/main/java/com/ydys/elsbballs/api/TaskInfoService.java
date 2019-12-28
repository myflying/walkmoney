package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.TaskInfoWrapperRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface TaskInfoService {

    @POST("v2.Task/taskList")
    Observable<TaskInfoWrapperRet> taskList(@Body RequestBody requestBody);
}
