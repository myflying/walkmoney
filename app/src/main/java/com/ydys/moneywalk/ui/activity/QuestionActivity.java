package com.ydys.moneywalk.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.QuestionCategoryRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.presenter.QuestionInfoPresenterImp;
import com.ydys.moneywalk.ui.adapter.QuestionAdapter;
import com.ydys.moneywalk.ui.adapter.QuestionCategoryAdapter;
import com.ydys.moneywalk.ui.custom.NormalDecoration;
import com.ydys.moneywalk.view.QuestionInfoView;

import butterknife.BindView;
import butterknife.OnClick;

public class QuestionActivity extends BaseActivity implements QuestionInfoView {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.question_category_view)
    RecyclerView questionCategoryListView;

    QuestionInfoPresenterImp questionInfoPresenterImp;

    QuestionCategoryAdapter questionCategoryAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_question;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void initVars() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 0);
    }

    @Override
    protected void initViews() {
        mTitleTv.setTextColor(ContextCompat.getColor(this, R.color.black));
        mTitleTv.setText("常见问题");
        questionCategoryAdapter = new QuestionCategoryAdapter(this, null);
        questionCategoryListView.setLayoutManager(new LinearLayoutManager(this));
        questionCategoryListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.bg_color), SizeUtils.dp2px(10)));
        questionCategoryListView.setAdapter(questionCategoryAdapter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        questionInfoPresenterImp = new QuestionInfoPresenterImp(this, this);
        questionInfoPresenterImp.loadQuestionList();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(QuestionCategoryRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                questionCategoryAdapter.setNewData(tData.getData());
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }
}
