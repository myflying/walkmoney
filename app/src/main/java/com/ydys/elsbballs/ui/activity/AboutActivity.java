package com.ydys.elsbballs.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.AppUtils;
import com.jaeger.library.StatusBarUtil;
import com.ydys.elsbballs.R;
import com.ydys.elsbballs.presenter.Presenter;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.tv_version_name)
    TextView mVersionNameTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
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
        mTitleTv.setText("关于我们");

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mVersionNameTv.setText("V" + AppUtils.getAppVersionName());
    }

    @OnClick(R.id.tv_xieyi)
    void xieyi() {
        Intent intent = new Intent(this, PrivacyActivity.class);
        intent.putExtra("show_type", 0);
        startActivity(intent);
    }

    @OnClick(R.id.tv_privacy)
    void privacy() {
        Intent intent = new Intent(this, PrivacyActivity.class);
        intent.putExtra("show_type", 1);
        startActivity(intent);
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

}
