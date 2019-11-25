package com.ydys.moneywalk.ui.fragment;

import android.content.Intent;

import com.ydys.moneywalk.R;
import com.ydys.moneywalk.ui.activity.AboutActivity;
import com.ydys.moneywalk.ui.activity.BodyDataActivity;
import com.ydys.moneywalk.ui.activity.CashActivity;
import com.ydys.moneywalk.ui.activity.LoginActivity;
import com.ydys.moneywalk.ui.activity.MyWalletActivity;
import com.ydys.moneywalk.ui.activity.PhoneLoginActivity;
import com.ydys.moneywalk.ui.activity.SettingActivity;

import butterknife.OnClick;

public class MyFragment extends BaseFragment {

    @Override
    protected int getContentView() {
        return R.layout.fragment_my;
    }

    @Override
    public void initVars() {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.layout_user_info)
    void login() {
        Intent intent = new Intent(getActivity(), PhoneLoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_body_data)
    void bodyData() {
        Intent intent = new Intent(getActivity(), BodyDataActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_setting)
    void setting() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_cash_now)
    void cashNow() {
        Intent intent = new Intent(getActivity(), CashActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_my_wallet)
    void myWallet() {
        Intent intent = new Intent(getActivity(), MyWalletActivity.class);
        startActivity(intent);
    }
}
