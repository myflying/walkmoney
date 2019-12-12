package com.ydys.moneywalk.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.UserInfo;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.presenter.UserInfoPresenterImp;
import com.ydys.moneywalk.view.UserInfoView;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class UserInfoActivity extends BaseActivity implements UserInfoView {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.iv_user_head)
    ImageView mUserHeadIv;

    @BindView(R.id.tv_user_nick_name)
    TextView mNickNameTv;

    @BindView(R.id.tv_user_invite_code)
    TextView mUserInviteCodeTv;

    @BindView(R.id.tv_mobile)
    TextView mMobileTv;

    @BindView(R.id.tv_wx_number)
    TextView mWxNumberTv;

    UserInfo mUserInfo;

    private UMShareAPI mShareAPI = null;

    private ProgressDialog progressDialog = null;

    UserInfoPresenterImp userInfoPresenterImp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
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
        mTitleTv.setText("个人中心");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录");

        mShareAPI = UMShareAPI.get(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userInfoPresenterImp = new UserInfoPresenterImp(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (App.mUserInfo != null) {
            mUserInfo = App.mUserInfo;
            //头像
            RequestOptions options = new RequestOptions();
            options.override(SizeUtils.dp2px(34), SizeUtils.dp2px(34));
            options.error(R.mipmap.def_head);
            options.placeholder(R.mipmap.def_head);
            options.transform(new RoundedCorners(SizeUtils.dp2px(34)));
            Glide.with(this).load(mUserInfo.getFace()).apply(options).into(mUserHeadIv);

            mNickNameTv.setText(StringUtils.isEmpty(mUserInfo.getNickname()) ? "走路宝" + mUserInfo.getId() : mUserInfo.getNickname());
            mUserInviteCodeTv.setText(mUserInfo.getId() + "");
            if (mUserInfo.getBindMobile() == 1) {
                mMobileTv.setText("已绑定");
                mMobileTv.setClickable(false);
            } else {
                mMobileTv.setText("去绑定");
                mMobileTv.setClickable(true);
            }

            if (mUserInfo.getBindWechat() == 1) {
                mWxNumberTv.setText("已绑定");
                mWxNumberTv.setClickable(false);
            } else {
                mWxNumberTv.setText("去绑定");
                mWxNumberTv.setClickable(true);
            }
        }
    }

    @OnClick(R.id.tv_mobile)
    public void bindPhone() {
        Intent intent = new Intent(this, BindPhoneActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_wx_number)
    public void bindWx() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
        mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(UserInfoRet tData) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        Logger.i(JSON.toJSONString(tData));

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                Toasty.normal(UserInfoActivity.this, "登录成功").show();
                //存储用户信息
                SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(tData.getData()));
                SPUtils.getInstance().put(Constants.LOCAL_LOGIN, true);
                App.mUserInfo = tData.getData();
                App.isLogin = true;
                finish();
            } else {
                Toasty.normal(UserInfoActivity.this, tData.getMsg()).show();
            }
        } else {
            Toasty.normal(UserInfoActivity.this, tData.getMsg()).show();
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        Toasty.normal(UserInfoActivity.this, "系统错误").show();
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Logger.i("auth data --->" + JSONObject.toJSONString(data));
            //Toast.makeText(mContext, "授权成功了", Toast.LENGTH_LONG).show();
            try {
                App.isLogin = true;
                if (data != null) {
                    Logger.i("wx openid--->" + data.get("openid"));

                    userInfoPresenterImp.login(PhoneUtils.getIMEI(), "wechat", data.get("openid"), "", data.get("name"), data.get("iconurl"));

                    Logger.i("wx login info--->" + data.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            //Toast.makeText(mContext, "授权失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            Toasty.normal(UserInfoActivity.this, "授权失败").show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toasty.normal(UserInfoActivity.this, "授权取消").show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
