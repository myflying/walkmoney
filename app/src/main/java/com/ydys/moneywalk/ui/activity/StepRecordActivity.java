package com.ydys.moneywalk.ui.activity;

import android.os.Bundle;

import com.ydys.moneywalk.R;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.custom.CircleProgress;

import butterknife.BindView;

public class StepRecordActivity extends BaseActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_step_record;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //初始化计步模块
    }
}
