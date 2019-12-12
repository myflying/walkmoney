package com.ydys.moneywalk.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.StringUtils;
import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.FillInCodeInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.FillInCodeInfoPresenterImp;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.view.FillInCodeView;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class FillInCodeActivity extends BaseActivity implements FillInCodeView {

    @BindView(R.id.et_code)
    EditText mCodeEt;

    FillInCodeInfoPresenterImp fillInCodeInfoPresenterImp;

    private ProgressDialog progressDialog = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fill_in_code;
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
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        fillInCodeInfoPresenterImp = new FillInCodeInfoPresenterImp(this, this);
    }

    @OnClick(R.id.btn_commit_code)
    void commitCode() {
        if (StringUtils.isEmpty(mCodeEt.getText())) {
            Toasty.normal(FillInCodeActivity.this, "请输入邀请码").show();
            return;
        }

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        fillInCodeInfoPresenterImp.fillInCode(App.mUserInfo != null ? App.mUserInfo.getId() : "", mCodeEt.getText().toString());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(FillInCodeInfoRet tData) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            Toasty.normal(FillInCodeActivity.this, "填写成功").show();

            Intent intent = new Intent();
            setResult(1, intent);
            finish();
        } else {
            Toasty.normal(FillInCodeActivity.this, tData.getMsg()).show();
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toasty.normal(FillInCodeActivity.this, "系统错误").show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    @OnClick(R.id.iv_back)
    void back() {
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }
}
