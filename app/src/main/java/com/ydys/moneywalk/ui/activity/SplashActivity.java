package com.ydys.moneywalk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ydys.moneywalk.R;
import com.ydys.moneywalk.presenter.Presenter;

/**
 * Created by myflying on 2019/1/3.
 */
public class SplashActivity extends BaseActivity {

    Handler handler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
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
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,GameActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }

}
