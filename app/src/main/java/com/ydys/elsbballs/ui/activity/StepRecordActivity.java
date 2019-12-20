package com.ydys.elsbballs.ui.activity;

import android.os.Bundle;

import com.ydys.elsbballs.R;
import com.ydys.elsbballs.presenter.Presenter;

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
