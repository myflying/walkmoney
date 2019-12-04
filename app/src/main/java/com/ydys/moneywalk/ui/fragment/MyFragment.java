package com.ydys.moneywalk.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
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
import com.ydys.moneywalk.ui.activity.FillInCodeActivity;
import com.ydys.moneywalk.ui.activity.InviteFriendActivity;
import com.ydys.moneywalk.ui.activity.LoginActivity;
import com.ydys.moneywalk.ui.activity.MakeMoneyActivity;
import com.ydys.moneywalk.ui.activity.MyWalletActivity;
import com.ydys.moneywalk.ui.activity.PhoneLoginActivity;
import com.ydys.moneywalk.ui.activity.SettingActivity;
import com.ydys.moneywalk.ui.custom.GlideCircleTransformWithBorder;
import com.ydys.moneywalk.view.InitInfoView;
import com.ydys.moneywalk.view.UserInfoView;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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

    @BindView(R.id.iv_can_cash)
    ImageView mCanCashIv;

    @BindView(R.id.layout_user_account)
    LinearLayout mUserAccountLayout;

    @BindView(R.id.btn_cash_now)
    Button mCashMoneyBtn;

    @BindView(R.id.tv_copy_code)
    TextView mCopyCodeTv;

    private UserInfoPresenterImp userInfoPresenterImp;

    private UserInfo mUserInfo;

    private boolean isRequestInfo;

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
        isRequestInfo = true;
        if (isRequestInfo) {
            Logger.i("myfragment onresume--->");
            userInfoPresenterImp.imeiLogin(PhoneUtils.getIMEI(), "10000", "yangcheng");
        }
    }

    public void loadUserInfo() {
        if (!isRequestInfo) {
            Logger.i("myfragment load user info--->");
            userInfoPresenterImp.imeiLogin(PhoneUtils.getIMEI(), "10000", "yangcheng");
        }
    }

    @OnClick(R.id.layout_user_info)
    void login() {
        if (!SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            if (App.mUserInfo != null) {
                if (App.mUserInfo.getIsBind() == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    @OnClick(R.id.layout_body_data)
    void bodyData() {
        if (!SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            if (App.mUserInfo != null) {
                Intent intent = new Intent(getActivity(), BodyDataActivity.class);
                startActivity(intent);
            }
        }
    }

    @OnClick(R.id.layout_setting)
    void setting() {
        if (!SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            if (App.mUserInfo != null) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        }
    }

    @OnClick(R.id.layout_my_wallet)
    void myWallet() {
        if (!SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            if (App.mUserInfo != null) {
                if (App.mUserInfo.getIsBind() == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), MyWalletActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    @OnClick({R.id.layout_cash_money, R.id.btn_cash_now})
    void myCashMoney() {
        if (!SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            if (App.mUserInfo != null) {
                if (App.mUserInfo.getIsBind() == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), CashActivity.class);
                    startActivity(intent);
                }
            }
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
        isRequestInfo = false;
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                mUserInfo = tData.getData();

                mUserGoldNumTv.setText(mUserInfo.getGold() + "");
                mCashMoneyTv.setText(mUserInfo.getAmount() + "");

                //头像
                RequestOptions options = new RequestOptions();
                options.override(SizeUtils.dp2px(50), SizeUtils.dp2px(50));
                options.error(R.mipmap.def_head);
                options.placeholder(R.mipmap.def_head);
                options.transform(new GlideCircleTransformWithBorder(getActivity(), 1, ContextCompat.getColor(getActivity(), R.color.white)));

                String nickName = "游客" + mUserInfo.getId();
                String inviteCode = "点击登录";

                if (mUserInfo.getIsBind() == 1) {
                    if (SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false)) {
                        nickName = StringUtils.isEmpty(mUserInfo.getNickname()) ? "走路宝" + mUserInfo.getId() : mUserInfo.getNickname();
                        inviteCode = "邀请码：" + mUserInfo.getId();
                        mCashMoneyBtn.setBackgroundResource(R.mipmap.cash_btn_bg);
                        Glide.with(getActivity()).load(mUserInfo.getFace()).apply(options).into(mUserHeadIv);
                        mCanCashIv.setVisibility(View.VISIBLE);
                    } else {
                        nickName = "点击登录";
                        inviteCode = "让走路更有趣";
                        Glide.with(getActivity()).load(R.mipmap.def_head).apply(options).into(mUserHeadIv);

                        mUserGoldNumTv.setText("--");
                        mCashMoneyTv.setText("--");
                        mCanCashIv.setVisibility(View.INVISIBLE);
                        mCashMoneyBtn.setBackgroundResource(R.drawable.cash_btn_normal_bg);
                        mCopyCodeTv.setVisibility(View.GONE);
                    }
                } else {
                    mCanCashIv.setVisibility(View.VISIBLE);
                    mCopyCodeTv.setVisibility(View.GONE);
                    Glide.with(getActivity()).load(mUserInfo.getFace()).apply(options).into(mUserHeadIv);
                }

                mUserNickNameTv.setText(nickName);
                mUserDescTv.setText(inviteCode);
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        isRequestInfo = false;
    }

    @OnClick(R.id.tv_copy_code)
    void copyCode() {
        copyTextToSystem(getActivity(), App.mUserInfo != null ? App.mUserInfo.getId() : "");
        ToastUtils.showLong("已复制");
    }

    /**
     * 复制文本到系统剪切板
     */
    public static void copyTextToSystem(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", text);
        cm.setPrimaryClip(mClipData);
    }

    @OnClick(R.id.layout_fill_in)
    void fillInCode() {
        Intent intent = new Intent(getActivity(), FillInCodeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_invite_friend)
    void inviteFriend() {
        Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
        intent.putExtra("share_type", 1);
        startActivity(intent);
    }

    @OnClick(R.id.layout_mk_money)
    void makeMoney() {
        Intent intent = new Intent(getActivity(), MakeMoneyActivity.class);
        startActivity(intent);
    }
}
