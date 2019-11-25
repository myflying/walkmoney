package com.ydys.moneywalk.ui.activity;

import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.custom.ActRuleDialog;

import butterknife.OnClick;

public class MakeMoneyActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_make_money;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }
}
