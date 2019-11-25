package com.ydys.moneywalk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.custom.LoginOutDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity implements LoginOutDialog.LoginOutListener {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    LoginOutDialog loginOutDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
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
        mTitleTv.setText("设置");

        loginOutDialog = new LoginOutDialog(this, R.style.common_dialog);
        loginOutDialog.setLoginOutListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.layout_user_info)
    void userInfo() {
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_about_us)
    void about() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_login_out)
    void loginOut() {
        if (loginOutDialog != null && !loginOutDialog.isShowing()) {
            loginOutDialog.show();
        }
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

    @Override
    public void configLoginOut() {
        ToastUtils.showLong("已退出");
    }

    @Override
    public void cancelLoginOut() {

    }
}
