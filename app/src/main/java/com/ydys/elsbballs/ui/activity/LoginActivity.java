package com.ydys.elsbballs.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ydys.elsbballs.App;
import com.ydys.elsbballs.R;
import com.ydys.elsbballs.bean.UserInfoRet;
import com.ydys.elsbballs.common.Constants;
import com.ydys.elsbballs.presenter.Presenter;
import com.ydys.elsbballs.presenter.UserInfoPresenterImp;
import com.ydys.elsbballs.view.UserInfoView;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends BaseActivity implements UserInfoView {

    @BindView(R.id.iv_agree)
    ImageView mAgreeIv;

    private UMShareAPI mShareAPI = null;

    private ProgressDialog progressDialog = null;

    UserInfoPresenterImp userInfoPresenterImp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录");

        mShareAPI = UMShareAPI.get(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userInfoPresenterImp = new UserInfoPresenterImp(this, this);
    }

    @OnClick(R.id.layout_wx_login)
    void wxLogin() {
        if (!SPUtils.getInstance().getBoolean("agree_xieyi", true)) {
            ToastUtils.showLong("请先同意服务协议及隐私政策");
            return;
        }
        mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
    }

    @OnClick(R.id.layout_phone_login)
    void phoneLogin() {
        if (!SPUtils.getInstance().getBoolean("agree_xieyi", true)) {
            ToastUtils.showLong("请先同意服务协议及隐私政策");
            return;
        }
        Intent intent = new Intent(this, PhoneLoginActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.iv_agree)
    void chooseXieYi() {
        boolean temp = !SPUtils.getInstance().getBoolean("agree_xieyi");
        mAgreeIv.setImageResource(temp ? R.mipmap.agree_selected : R.mipmap.agree_normal);
        SPUtils.getInstance().put("agree_xieyi", temp);
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
                    if (progressDialog != null && !progressDialog.isShowing()) {
                        progressDialog.show();
                    }
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
            Toasty.normal(LoginActivity.this, "授权失败").show();
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
            Toasty.normal(LoginActivity.this, "授权取消").show();
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
                Toasty.normal(LoginActivity.this, "登录成功").show();
                //存储用户信息
                SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(tData.getData()));
                SPUtils.getInstance().put(Constants.LOCAL_LOGIN, true);
                App.mUserInfo = tData.getData();
                App.mUserInfo.setIsBind(1);
                App.isLogin = true;
                finish();
            } else {
                App.mUserInfo =null;
                App.isLogin = false;
                SPUtils.getInstance().put(Constants.LOCAL_LOGIN, false);

                Toasty.normal(LoginActivity.this, tData.getMsg()).show();
            }
        } else {
            App.mUserInfo =null;
            App.isLogin = false;
            SPUtils.getInstance().put(Constants.LOCAL_LOGIN, false);

            Toasty.normal(LoginActivity.this, tData.getMsg()).show();
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        Toasty.normal(LoginActivity.this, "系统错误").show();
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

}
