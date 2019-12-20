package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.bean.QuestionCategoryRet;
import com.ydys.elsbballs.model.QuestionInfoModelImp;
import com.ydys.elsbballs.view.QuestionInfoView;

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
