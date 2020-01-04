package com.ydys.elsbballs.ui.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ydys.elsbballs.presenter.Presenter;

import butterknife.ButterKnife;

/**
 * Created by admin on 2017/4/8.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected Context context;

    protected Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setStatusBar();
        context = this;
        ButterKnife.bind(this);
        initVars();
        initViews();
        initData(savedInstanceState);
    }

    public void setStatusBar() {
        //StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
        //invadeStatusBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TeaAgent.onResume(this);
        //MobclickAgent.onResume(this);
        if (presenter == null && getPresenter() != null) {
            presenter = getPresenter();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TeaAgent.onPause(this);
        //MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (presenter != null) {
            presenter.destroy();
        }
    }

    public Context getContext() {
        return context;
    }

    protected abstract int getLayoutId();

    protected abstract Presenter getPresenter();

    protected abstract void initVars();

    protected abstract void initViews();

    protected abstract void initData(Bundle savedInstanceState);
}
