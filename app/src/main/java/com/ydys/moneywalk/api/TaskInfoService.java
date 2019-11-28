package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.InitInfoRet;
import com.ydys.moneywalk.bean.TaskInfoWrapperRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface TaskInfoService {

    @POST("v1.Task/taskList")
    Observable<TaskInfoWrapperRet> taskList(@Body RequestBody requestBody);
}
