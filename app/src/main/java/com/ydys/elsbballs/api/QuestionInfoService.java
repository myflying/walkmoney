package com.ydys.elsbballs.api;

import com.ydys.elsbballs.bean.QuestionCategoryRet;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by admin on 2017/4/7.
 */

public interface QuestionInfoService {

    @POST("v2.Common/question")
    Observable<QuestionCategoryRet> loadQuestionList(@Body RequestBody requestBody);

}
