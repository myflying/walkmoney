package com.ydys.elsbballs.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.orhanobut.logger.Logger;
import com.ydys.elsbballs.App;
import com.ydys.elsbballs.R;
import com.ydys.elsbballs.bean.UserInfo;
import com.ydys.elsbballs.bean.UserInfoRet;
import com.ydys.elsbballs.common.Constants;
import com.ydys.elsbballs.presenter.UserInfoPresenterImp;
import com.ydys.elsbballs.ui.activity.BodyDataActivity;
import com.ydys.elsbballs.ui.activity.CashActivity;
import com.ydys.elsbballs.ui.activity.FillInCodeActivity;
import com.ydys.elsbballs.ui.activity.InviteFriendActivity;
import com.ydys.elsbballs.ui.activity.LoginActivity;
import com.ydys.elsbballs.ui.activity.MakeMoneyActivity;
import com.ydys.elsbballs.ui.activity.MyWalletActivity;
import com.ydys.elsbballs.ui.activity.SettingActivity;
import com.ydys.elsbballs.ui.custom.DislikeDialog;
import com.ydys.elsbballs.ui.custom.GlideCircleTransformWithBorder;
import com.ydys.elsbballs.util.MatrixUtils;
import com.ydys.elsbballs.util.TTAdManagerHolder;
import com.ydys.elsbballs.view.UserInfoView;

import java.util.List;

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

    @BindView(R.id.iv_can_cash)
    ImageView mCanCashIv;

    @BindView(R.id.layout_user_account)
    LinearLayout mUserAccountLayout;

    @BindView(R.id.btn_cash_now)
    Button mCashMoneyBtn;

    @BindView(R.id.tv_copy_code)
    TextView mCopyCodeTv;

    @BindView(R.id.layout_fill_in)
    LinearLayout mLayoutFillIn;

    @BindView(R.id.express_container)
    FrameLayout mExpressContainer;

    private UserInfoPresenterImp userInfoPresenterImp;

    private UserInfo mUserInfo;

    private boolean isRequestInfo;

    //广告配置
    private TTAdNative mTTAdNative;

    private TTAdDislike mTTAdDislike;

    private TTNativeExpressAd mTTAd;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    if (adView != null) {
                        mExpressContainer.addView(adView);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_my;
    }

    @Override
    public void initVars() {

    }

    @Override
    public void initViews() {
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(getActivity());
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
            userInfoPresenterImp.imeiLogin(PhoneUtils.getIMEI(), App.agentId, App.softId,App.appName);
        }
        loadExpressAd(Constants.MY_BANNER_CODE_ID);
    }

    public void loadUserInfo() {
        if (!isRequestInfo) {
            Logger.i("myfragment load user info--->");
            userInfoPresenterImp.imeiLogin(PhoneUtils.getIMEI(), App.agentId, App.softId,App.appName);
        }
        mLayoutFillIn.setVisibility(SPUtils.getInstance().getBoolean(Constants.INVITE_WRITE_CODE) ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.layout_user_info, R.id.iv_user_head})
    void login() {
        if (!App.isLogin) {
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
        if (!App.isLogin) {
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
        if (!App.isLogin) {
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
        if (!App.isLogin) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            if (App.mUserInfo != null) {
                if (App.mUserInfo.getIsBind() == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), MyWalletActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    @OnClick({R.id.layout_cash_money, R.id.btn_cash_now})
    void myCashMoney() {
        if (!App.isLogin) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            if (App.mUserInfo != null) {
                if (App.mUserInfo.getIsBind() == 0) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
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
                mCashMoneyTv.setText(MatrixUtils.getPrecisionMoney(mUserInfo.getAmount()));

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
                        nickName = StringUtils.isEmpty(mUserInfo.getNickname()) ? "2048弹弹球" + mUserInfo.getId() : mUserInfo.getNickname();
                        inviteCode = "邀请码：" + mUserInfo.getId();
                        mCashMoneyBtn.setBackgroundResource(R.mipmap.cash_btn_bg);
                        Glide.with(getActivity()).load(mUserInfo.getFace()).apply(options).into(mUserHeadIv);
                        mCanCashIv.setVisibility(View.VISIBLE);
                    } else {
                        nickName = "点击登录";
                        inviteCode = "让游戏更有趣";
                        Glide.with(getActivity()).load(R.mipmap.def_head).apply(options).into(mUserHeadIv);

                        mUserGoldNumTv.setText("--");
                        mCashMoneyTv.setText("--");
                        mCanCashIv.setVisibility(View.INVISIBLE);
                        mCashMoneyBtn.setBackgroundResource(R.mipmap.not_login_bg);
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
        if (!App.isLogin) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), FillInCodeActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.layout_invite_friend)
    void inviteFriend() {
        if (!App.isLogin) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
            intent.putExtra("share_type", 1);
            startActivity(intent);
        }
    }

    @OnClick(R.id.layout_mk_money)
    void makeMoney() {
        Intent intent = new Intent(getActivity(), MakeMoneyActivity.class);
        startActivity(intent);
    }


    //广告的加载
    private void loadExpressAd(String codeId) {
        //关闭广告弹窗时，移除adView
        if (adView != null && adView.getParent() != null) {
            ((ViewGroup) adView.getParent()).removeView(adView);
        }

        mExpressContainer.removeAllViews();
        float expressViewWidth = 350;
        float expressViewHeight = 233;

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("load error : " + code + ", " + message);
                mExpressContainer.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
//                mTTAd.setSlideIntervalTime(30*1000);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
                mTTAd.render();
            }
        });
    }

    private long startTime = 0;

    private boolean mHasShowDownloadActive = false;

    private View adView;

    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Logger.i("广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Logger.i("广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Logger.i("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
                Logger.i(msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
                Logger.i("渲染成功" + view.getWidth() + "---height--->" + view.getHeight() + "---title--->");
                mExpressContainer.removeAllViews();
                adView = view;
                Message message = Message.obtain();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        });
        //dislike设置
        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                Logger.i("点击开始下载");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    Logger.i("下载中，点击暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                Logger.i("下载暂停，点击继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                Logger.i("下载失败，点击重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                Logger.i("安装完成，点击图片打开");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                Logger.i("点击安装");
            }
        });
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(getActivity(), words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    Logger.i("点击 " + filterWord.getName());
                    //用户选择不喜欢原因后，移除广告展示
                    mExpressContainer.removeAllViews();
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(getActivity(), new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Logger.i("点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                mExpressContainer.removeAllViews();
            }

            @Override
            public void onCancel() {
                Logger.i("点击取消 ");
            }
        });
    }
}
