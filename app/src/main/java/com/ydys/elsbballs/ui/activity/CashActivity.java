package com.ydys.elsbballs.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.model.GuidePage;
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
import com.ydys.elsbballs.App;
import com.ydys.elsbballs.R;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.CashInitInfo;
import com.ydys.elsbballs.bean.CashInitInfoRet;
import com.ydys.elsbballs.bean.CashMoneyItem;
import com.ydys.elsbballs.bean.CashMoneyRet;
import com.ydys.elsbballs.bean.UserInfoRet;
import com.ydys.elsbballs.common.Constants;
import com.ydys.elsbballs.presenter.CashInitInfoPresenterImp;
import com.ydys.elsbballs.presenter.CashMoneyPresenterImp;
import com.ydys.elsbballs.presenter.Presenter;
import com.ydys.elsbballs.presenter.UserInfoPresenterImp;
import com.ydys.elsbballs.ui.adapter.CashMoneyAdapter;
import com.ydys.elsbballs.ui.custom.CommonDialog;

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

    //private double cashMoney = 0;

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
                //cashMoney = cashMoneyAdapter.getData().get(lastIndex).getAmount();
                lastIndex = position;
                cashMoneyAdapter.notifyDataSetChanged();
            }
        });

        if (SPUtils.getInstance().getInt(Constants.GUIDE_STEP, 0) < 4) {
            showGuideFour();
        }
    }

    //第4个页面引导
    public void showGuideFour() {
        NewbieGuide.with(this)
                .setLabel("guide3")
                .alwaysShow(true)//总是显示，调试时可以打开
                .addGuidePage(GuidePage.newInstance().setLayoutRes(R.layout.guide_view_four, R.id.layout_cash_now, R.id.iv_guide_bottom).setEverywhereCancelable(false))
                .setOnGuideChangedListener(new OnGuideChangedListener() {
                    @Override
                    public void onShowed(Controller controller) {
                        //Toast.makeText(CashActivity.this, "引导层显示", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRemoved(Controller controller) {
                        //Toast.makeText(CashActivity.this, "引导层消失", Toast.LENGTH_SHORT).show();
                        SPUtils.getInstance().put(Constants.GUIDE_STEP, 4);
                    }
                })
                .show();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userInfoPresenterImp = new UserInfoPresenterImp(this, this);
        cashInitInfoPresenterImp = new CashInitInfoPresenterImp(this, this);
        cashMoneyPresenterImp = new CashMoneyPresenterImp(this, this);
        cashInitInfoPresenterImp.cashInitMoney(App.mUserInfo != null ? App.mUserInfo.getId() : "");
    }

    @OnClick(R.id.btn_cash_record)
    void cashRecord() {
        Intent intent = new Intent(this, CashRecordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.cash_gif)
    void cashNow() {
        //未绑定微信
        if (cashInitInfo.getUserInfo().getBindWechat() == 0) {
            if (bindPhoneDialog != null && !bindPhoneDialog.isShowing()) {
                bindPhoneDialog.show();
                bindPhoneDialog.setDialogInfo("绑定微信", "为了你的账号安全，请先绑定微信");
            }
            return;
        }

        if (cashInitInfo.getUserInfo().getGold() < cashMoneyItem.getNeedGold()) {
            Toasty.normal(CashActivity.this, "金币余额不足").show();
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
                    lastIndex = 0;
                    cashMoneyItem = cashItems.get(lastIndex);
                    cashItems.get(lastIndex).setSelected(true);
                    mNeedGoldTv.setText(cashItems.get(lastIndex).getNeedGold() + "");
                    //cashMoney = cashItems.get(0).getAmount();
                    cashMoneyAdapter.setNewData(cashItems);
                }
            }

            if (tData instanceof UserInfoRet) {
                if (((UserInfoRet) tData).getCode() == Constants.SUCCESS) {
                    Toasty.normal(CashActivity.this, "绑定成功").show();

                    //存储用户信息
                    SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(((UserInfoRet) tData).getData()));
                    SPUtils.getInstance().put(Constants.LOCAL_LOGIN, true);
                    App.mUserInfo = ((UserInfoRet) tData).getData();
                    App.isLogin = true;

                    cashInitInfo.getUserInfo().setBindWechat(1);

                    RequestOptions options = new RequestOptions();
                    options.override(SizeUtils.dp2px(18), SizeUtils.dp2px(18));
                    options.error(R.mipmap.def_head);
                    options.placeholder(R.mipmap.def_head);
                    options.transform(new RoundedCornersTransformation(SizeUtils.dp2px(9), 0));
                    Glide.with(this).load(App.mUserInfo.getFace()).apply(options).into(mWxHeadIv);
                    mWxNickNameTv.setText(App.mUserInfo.getNickname());
                    mGotoBindTv.setVisibility(View.GONE);
                    mBindRightIv.setVisibility(View.GONE);
                } else {
                    Toasty.normal(CashActivity.this, ((UserInfoRet) tData).getMsg()).show();
                }
            }

            if (tData instanceof CashMoneyRet) {
                Toasty.normal(CashActivity.this, ((CashMoneyRet) tData).getMsg()).show();
                //提现成功，重新刷新页面
                if (((CashMoneyRet) tData).getCode() == Constants.SUCCESS) {
                    SPUtils.getInstance().put("is_show_cash_point", false);
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
        if (requestCode == 1) {
            Logger.i("reload list--->");
            cashInitInfoPresenterImp.cashInitMoney(App.mUserInfo != null ? App.mUserInfo.getId() : "");
        }
    }

    @Override
    public void commonConfig() {
        bindWx();
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
