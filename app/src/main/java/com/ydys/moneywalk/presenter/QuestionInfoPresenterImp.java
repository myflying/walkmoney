package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.bean.QuestionCategoryRet;
import com.ydys.moneywalk.model.QuestionInfoModelImp;
import com.ydys.moneywalk.view.QuestionInfoView;

/**
 * Created by admin on 2017/4/7.
 */

public class QuestionInfoPresenterImp extends BasePresenterImp<QuestionInfoView, QuestionCategoryRet> implements QuestionInfoPresenter {

    private Context context = null;
    private QuestionInfoModelImp questionInfoModelImp = null;

    public QuestionInfoPresenterImp(QuestionInfoView view, Context context) {
        super(view);
        this.context = context;
        this.questionInfoModelImp = new QuestionInfoModelImp(context);
    }

    @Override
    public void loadQuestionList() {
        questionInfoModelImp.loadQuestionList(this);
    }
}
