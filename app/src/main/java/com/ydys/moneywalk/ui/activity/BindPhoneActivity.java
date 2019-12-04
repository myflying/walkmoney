package com.ydys.moneywalk.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.SendMsgInfoRet;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.presenter.SendMsgInfoPresenterImp;
import com.ydys.moneywalk.presenter.UserInfoPresenterImp;
import com.ydys.moneywalk.util.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class BindPhoneActivity extends BaseActivity implements IBaseView {

    @BindView(R.id.et_phone_number)
    EditText mPhoneNumberEt;

    @BindView(R.id.et_validate_code)
    EditText mValidateCodeEt;

    @BindView(R.id.layout_get_code)
    LinearLayout mGetCodeLayout;

    @BindView(R.id.tv_get_code)
    TextView mGetCodeTv;

    @BindView(R.id.layout_login)
    LinearLayout mLoginLayout;

    @BindView(R.id.tv_login)
    TextView mLoginTv;

    UserInfoPresenterImp userInfoPresenterImp;

    private SendMsgInfoPresenterImp sendMsgInfoPresenterImp;

    private ProgressDialog progressDialog = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_phone;
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

        mPhoneNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    mGetCodeLayout.setBackgroundResource(R.drawable.validate_focus_bg);
                    mGetCodeTv.setTextColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.white));
                    mGetCodeLayout.setClickable(true);
                } else {
                    mGetCodeLayout.setBackgroundResource(R.drawable.validate_normal_bg);
                    mGetCodeTv.setTextColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.common_title_color));
                    mGetCodeLayout.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mValidateCodeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 4) {
                    mLoginLayout.setClickable(true);
                    mLoginLayout.setBackgroundResource(R.mipmap.phone_login_bg);
                    mLoginTv.setTextColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.white));
                } else {
                    mLoginLayout.setClickable(false);
                    mLoginLayout.setBackgroundResource(R.drawable.phone_login_bg);
                    mLoginTv.setTextColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.common_title_color));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        sendMsgInfoPresenterImp = new SendMsgInfoPresenterImp(this, this);
        userInfoPresenterImp = new UserInfoPresenterImp(this, this);
    }

    @OnClick(R.id.layout_get_code)
    void getValidateCode() {
        if (StringUtils.isEmpty(mPhoneNumberEt.getText())) {
            ToastUtils.showLong("请输入手机号");
            return;
        }

        if (mPhoneNumberEt.getText().length() < 11) {
            ToastUtils.showLong("手机号格式错误");
            return;
        }

        startCountDown();
        mValidateCodeEt.requestFocus();
        sendMsgInfoPresenterImp.sendMsg(mPhoneNumberEt.getText().toString());
    }

    @OnClick(R.id.layout_login)
    void login() {
        if (StringUtils.isEmpty(mPhoneNumberEt.getText())) {
            ToastUtils.showLong("请输入手机号");
            return;
        }

        if (mPhoneNumberEt.getText().length() < 11) {
            ToastUtils.showLong("手机号格式错误");
            return;
        }

        if (com.blankj.utilcode.util.StringUtils.isEmpty(mValidateCodeEt.getText())) {
            ToastUtils.showLong("请输入验证码");
            return;
        }

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        userInfoPresenterImp.login(PhoneUtils.getIMEI(), "mobile", mPhoneNumberEt.getText().toString(), mValidateCodeEt.getText().toString(), "", "");
    }

    public void startCountDown() {
        mGetCodeLayout.setClickable(false);
        mGetCodeLayout.setBackgroundResource(R.drawable.validate_normal_bg);
        mGetCodeTv.setTextColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.common_title_color));

        CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                Logger.i("倒计时" + millisUntilFinished / 1000 + "秒");
                mGetCodeTv.setText(millisUntilFinished / 1000 + " s");
            }

            public void onFinish() {
                Logger.i("倒计时完成");
                mGetCodeTv.setText("获取验证码");
                mGetCodeLayout.setClickable(true);
                mGetCodeLayout.setBackgroundResource(R.drawable.validate_focus_bg);
                mGetCodeTv.setTextColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.white));
            }
        };
        timer.start();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(Object tData) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData != null) {
            if (tData instanceof SendMsgInfoRet && ((SendMsgInfoRet) tData).getCode() == Constants.SUCCESS) {
                Toasty.normal(this, "验证码已发送").show();
                Logger.i("send msg info --->" + JSON.toJSONString(tData));
            }

            if (tData instanceof UserInfoRet && ((UserInfoRet) tData).getCode() == Constants.SUCCESS) {
                Toasty.normal(BindPhoneActivity.this, "绑定成功").show();
                //存储用户信息
                SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(((UserInfoRet) tData).getData()));
                SPUtils.getInstance().put(Constants.LOCAL_LOGIN, true);
                App.mUserInfo = ((UserInfoRet) tData).getData();
                App.isLogin = true;

                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }
}
