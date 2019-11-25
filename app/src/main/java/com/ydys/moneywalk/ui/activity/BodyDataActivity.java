package com.ydys.moneywalk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.presenter.Presenter;

import butterknife.OnClick;

public class BodyDataActivity extends BaseActivity {

    BottomSheetDialog bottomSheetDialog;
    View sexView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_body_data;
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
        sexView = LayoutInflater.from(this).inflate(R.layout.choose_sex_view, null);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(sexView);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.layout_sex)
    void chooseSex() {
        if (bottomSheetDialog != null && !bottomSheetDialog.isShowing()) {
            bottomSheetDialog.show();
        }
    }

    @OnClick(R.id.layout_bmi)
    void bmiData() {
        Intent intent = new Intent(this, BMIDataActivity.class);
        startActivity(intent);
    }
}
