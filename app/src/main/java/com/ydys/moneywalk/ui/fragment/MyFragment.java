package com.ydys.moneywalk.ui.fragment;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.InitInfoRet;
import com.ydys.moneywalk.bean.UserInfo;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.InitInfoPresenterImp;
import com.ydys.moneywalk.presenter.UserInfoPresenterImp;
import com.ydys.moneywalk.ui.activity.AboutActivity;
import com.ydys.moneywalk.ui.activity.BodyDataActivity;
import com.ydys.moneywalk.ui.activity.CashActivity;
import com.ydys.moneywalk.ui.activity.LoginActivity;
import com.ydys.moneywalk.ui.activity.MyWalletActivity;
import com.ydys.moneywalk.ui.activity.PhoneLoginActivity;
import com.ydys.moneywalk.ui.activity.SettingActivity;
import com.ydys.moneywalk.view.InitInfoView;
import com.ydys.moneywalk.view.UserInfoView;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFragment extends BaseFragment implements UserInfoView {

    @BindView(R.id.iv_user_head)
    ImageView mUserHeadIv;

    @BindView(R.id.tv_user_nick_name)
    TextView mUserNickNameTv;

    @BindView(R.id.tv_user_desc)
    TextView mUserDescTv;

    @BindView(R.id.tv_user_gold_num)
    TextView mUserGoldNumTv;

    @BindView(R.id.tv_cash_money)
    TextView mCashMoneyTv;

    @BindView(R.id.layout_user_account)
    LinearLayout mUserAccountLayout;

    private UserInfoPresenterImp userInfoPresenterImp;

    private UserInfo mUserInfo;

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
        userInfoPresenterImp = new UserInfoPresenterImp(this, getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.i("myfragment onresume--->");
    }

    public void loadUserInfo() {
        Logger.i("load user info--->");
        userInfoPresenterImp.imeiLogin(PhoneUtils.getIMEI(), "10000", "yangcheng");
    }

    @OnClick(R.id.layout_user_info)
    void login() {
        if (mUserInfo != null) {
            //if (StringUtils.isEmpty(mUserInfo.getOpenid())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            // }
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
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

    @OnClick(R.id.layout_my_wallet)
    void myWallet() {
        if (mUserInfo != null) {
//            if (StringUtils.isEmpty(mUserInfo.getOpenid())) {
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//            } else {
//                Intent intent = new Intent(getActivity(), MyWalletActivity.class);
//                startActivity(intent);
//            }
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @OnClick({R.id.layout_cash_money, R.id.btn_cash_now})
    void myCashMoney() {
        if (mUserInfo != null) {
//            if (StringUtils.isEmpty(mUserInfo.getOpenid())) {
//                Intent intent = new Intent(getActivity(), LoginActivity.class);
//                startActivity(intent);
//            } else {
//                Intent intent = new Intent(getActivity(), CashActivity.class);
//                startActivity(intent);
//            }
        } else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(UserInfoRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                mUserInfo = tData.getData();
                RequestOptions options = new RequestOptions();
                options.error(R.mipmap.def_head);
                options.placeholder(R.mipmap.def_head);
                Glide.with(getActivity()).load(mUserInfo.getFace()).apply(options).into(mUserHeadIv);

                mUserNickNameTv.setText("游客" + mUserInfo.getId());
                mUserDescTv.setText("点击登录");
                mUserGoldNumTv.setText(mUserInfo.getGold() + "");
                mCashMoneyTv.setText(mUserInfo.getAmount() + "");

            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
