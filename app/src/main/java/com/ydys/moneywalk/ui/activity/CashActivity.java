package com.ydys.moneywalk.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.CashInitInfo;
import com.ydys.moneywalk.bean.CashInitInfoRet;
import com.ydys.moneywalk.bean.CashMoneyItem;
import com.ydys.moneywalk.bean.CashMoneyRet;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.CashInitInfoPresenterImp;
import com.ydys.moneywalk.presenter.CashMoneyPresenterImp;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.presenter.UserInfoPresenterImp;
import com.ydys.moneywalk.ui.adapter.CashMoneyAdapter;
import com.ydys.moneywalk.ui.custom.CommonDialog;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 提现页面
 */
public class CashActivity extends BaseActivity implements IBaseView, CommonDialog.CommonDialogListener {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.cash_money_list_view)
    RecyclerView mCashMoneyListView;

    @BindView(R.id.iv_wx_head)
    ImageView mWxHeadIv;

    @BindView(R.id.tv_wx_user_name)
    TextView mWxNickNameTv;

    @BindView(R.id.tv_go_to_bind)
    TextView mGotoBindTv;

    @BindView(R.id.tv_user_gold_num)
    TextView mUserGoldNumTv;

    @BindView(R.id.tv_cash_step_remark)
    TextView mCashStepRemark;

    @BindView(R.id.tv_need_gold)
    TextView mNeedGoldTv;

    @BindView(R.id.iv_wx_head_right)
    ImageView mBindRightIv;

    CashMoneyAdapter cashMoneyAdapter;

    private int lastIndex;

    CommonDialog bindPhoneDialog;

    CashInitInfoPresenterImp cashInitInfoPresenterImp;

    CashMoneyPresenterImp cashMoneyPresenterImp;

    CashInitInfo cashInitInfo;

    private UMShareAPI mShareAPI = null;

    private ProgressDialog progressDialog = null;

    UserInfoPresenterImp userInfoPresenterImp;

    private double cashMoney = 0;

    private CashMoneyItem cashMoneyItem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash;
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

        mTitleTv.setTextColor(ContextCompat.getColor(this, R.color.black));
        mTitleTv.setText("提现");

        bindPhoneDialog = new CommonDialog(this, R.style.common_dialog);
        bindPhoneDialog.setCommonDialogListener(this);

        cashMoneyAdapter = new CashMoneyAdapter(this, null);
        mCashMoneyListView.setLayoutManager(new GridLayoutManager(this, 3));
        mCashMoneyListView.setAdapter(cashMoneyAdapter);

        cashMoneyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == lastIndex) {
                    return;
                }
                cashMoneyItem = cashMoneyAdapter.getData().get(position);

                cashMoneyAdapter.getData().get(position).setSelected(true);
                mNeedGoldTv.setText(cashMoneyAdapter.getData().get(position).getNeedGold() + "");
                cashMoneyAdapter.getData().get(lastIndex).setSelected(false);
                cashMoney = cashMoneyAdapter.getData().get(lastIndex).getAmount();
                lastIndex = position;
                cashMoneyAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        cashInitInfoPresenterImp = new CashInitInfoPresenterImp(this, this);
        cashMoneyPresenterImp = new CashMoneyPresenterImp(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cashInitInfoPresenterImp.cashInitMoney(App.mUserInfo != null ? App.mUserInfo.getId() : "");
    }

    @OnClick(R.id.btn_cash_record)
    void cashRecord() {
        Intent intent = new Intent(this, CashRecordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.cash_gif)
    void cashNow() {
        //未绑定手机号
        if (cashInitInfo.getUserInfo().getBindMobile() == 0) {
            if (bindPhoneDialog != null && !bindPhoneDialog.isShowing()) {
                bindPhoneDialog.show();
                bindPhoneDialog.setDialogInfo("绑定手机提醒", "为了你的账号安全，请先绑定手机号");
            }
            return;
        }

        //当次提现是否验证过手机号
        if (!SPUtils.getInstance().getBoolean(Constants.THIS_CASH_VALIDATE, false)) {
            Intent intent = new Intent(this, ValidatePhoneActivity.class);
            startActivity(intent);
            return;
        }

        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage("正在提现");
            progressDialog.show();
        }
        cashMoneyPresenterImp.cashMoney(App.mUserInfo != null ? App.mUserInfo.getId() : "", cashMoneyItem.getAmount(), cashMoneyItem.getIsNewPeople());

    }

    @OnClick(R.id.tv_go_to_bind)
    void gotoBind() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(Object tData) {
        Logger.i(JSON.toJSONString(tData));
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (tData != null) {
            if (tData instanceof CashInitInfoRet && ((CashInitInfoRet) tData).getCode() == Constants.SUCCESS) {
                cashInitInfo = ((CashInitInfoRet) tData).getData();

                if (cashInitInfo.getUserInfo().getBindWechat() == 1) {
                    RequestOptions options = new RequestOptions();
                    options.override(SizeUtils.dp2px(18), SizeUtils.dp2px(18));
                    options.error(R.mipmap.def_head);
                    options.placeholder(R.mipmap.def_head);
                    options.transform(new RoundedCornersTransformation(SizeUtils.dp2px(9), 0));
                    Glide.with(this).load(cashInitInfo.getUserInfo().getFace()).apply(options).into(mWxHeadIv);
                    mWxNickNameTv.setText(cashInitInfo.getUserInfo().getNickname());
                    mGotoBindTv.setText("已绑定");
                    mBindRightIv.setVisibility(View.INVISIBLE);
                } else {
                    mWxHeadIv.setImageResource(R.mipmap.def_head);
                    mWxNickNameTv.setText("设置微信账户");
                    mGotoBindTv.setVisibility(View.VISIBLE);
                    mGotoBindTv.setText("去绑定");
                    mBindRightIv.setVisibility(View.VISIBLE);
                }

                mUserGoldNumTv.setText(cashInitInfo.getUserInfo().getGold() + "");
                mCashStepRemark.setText(Html.fromHtml(cashInitInfo.getCashOutConfig().getRule()));

                List<CashMoneyItem> cashItems = ((CashInitInfoRet) tData).getData().getCashMoneyItems();
                if (cashItems != null && cashItems.size() > 0) {
                    cashMoneyItem = cashItems.get(0);
                    cashItems.get(0).setSelected(true);
                    mNeedGoldTv.setText(cashItems.get(0).getNeedGold() + "");
                    cashMoney = cashItems.get(0).getAmount();
                    cashMoneyAdapter.setNewData(cashItems);
                }
            }

            if (tData instanceof UserInfoRet && ((UserInfoRet) tData).getCode() == Constants.SUCCESS) {
                Toasty.normal(CashActivity.this, "绑定成功").show();
                //存储用户信息
                SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(((UserInfoRet) tData).getData()));
                SPUtils.getInstance().put(Constants.LOCAL_LOGIN, true);
                App.mUserInfo = ((UserInfoRet) tData).getData();
                App.isLogin = true;
            }

            if (tData instanceof CashMoneyRet) {
                Toasty.normal(CashActivity.this, ((CashMoneyRet) tData).getMsg()).show();
                //提现成功，重新刷新页面
                if (((CashMoneyRet) tData).getCode() == Constants.SUCCESS) {
                    //重置手机验证状态
                    SPUtils.getInstance().put(Constants.THIS_CASH_VALIDATE, false);
                    cashInitInfoPresenterImp.cashInitMoney(App.mUserInfo != null ? App.mUserInfo.getId() : "");
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @OnClick(R.id.tv_go_to_bind)
    void bindWx() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
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
                    userInfoPresenterImp.login(PhoneUtils.getIMEI(), "wechat", data.get("openid"), "", data.get("name"), data.get("iconurl"));

                    Logger.i("wx openid--->" + data.get("openid"));
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
            Toasty.normal(CashActivity.this, "授权失败").show();
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
            Toasty.normal(CashActivity.this, "授权取消").show();
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

    @Override
    public void commonConfig() {
        Intent intent = new Intent(this, BindPhoneActivity.class);
        startActivity(intent);
    }

    @Override
    public void commonCancel() {

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
