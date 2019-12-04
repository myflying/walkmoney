package com.ydys.moneywalk.api;

import com.ydys.moneywalk.bean.QuestionCategoryRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface QuestionInfoService {

    @POST("v1.Common/question")
    Observable<QuestionCategoryRet> loadQuestionList(@Body RequestBody requestBody);

}
