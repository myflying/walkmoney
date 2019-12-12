package com.ydys.moneywalk.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.h5.core.YCGameClickCallback;
import com.yc.h5.core.YcGameAdCallback;
import com.yc.h5.core.YcGameAdShowCallback;
import com.yc.h5.core.YcGameDataCallback;
import com.yc.h5.core.YcGameInfo;
import com.yc.h5.core.YcGameInitCallback;
import com.yc.h5.core.YcGamePlayTimeCallback;
import com.yc.h5.core.YcGameSDK;
import com.yc.h5.core.YcGameWebView;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.InitInfoRet;
import com.ydys.moneywalk.bean.MessageEvent;
import com.ydys.moneywalk.bean.TakeGoldInfo;
import com.ydys.moneywalk.bean.TakeGoldInfoRet;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.InitInfoPresenterImp;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.presenter.TakeGoldInfoPresenterImp;
import com.ydys.moneywalk.presenter.UserInfoPresenterImp;
import com.ydys.moneywalk.ui.custom.DislikeDialog;
import com.ydys.moneywalk.ui.custom.HongBaoDialog;
import com.ydys.moneywalk.ui.custom.LoginDialog;
import com.ydys.moneywalk.ui.custom.PermissionDialog;
import com.ydys.moneywalk.ui.custom.ReceiveDoubleGoldDialog;
import com.ydys.moneywalk.ui.custom.ReceiveGoldDialog;
import com.ydys.moneywalk.util.MatrixUtils;
import com.ydys.moneywalk.util.RandomUtils;
import com.ydys.moneywalk.util.TTAdManagerHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class GameActivity extends BaseActivity implements YCGameClickCallback, YcGameDataCallback, YcGamePlayTimeCallback, YcGameAdShowCallback, IBaseView, PermissionDialog.PermissionConfigListener, LoginDialog.LoginListener, ReceiveGoldDialog.GoldDialogListener, ReceiveDoubleGoldDialog.GoldDoubleDialogListener {

    public static String TAG = "GameFragment";

    public static int BUBBLE_START = 1000;

    public static int BUBBLE_END = 2000;

    @BindView(R.id.layout_game_content)
    FrameLayout mGameLayout;

    @BindView(R.id.tv_game_gold_one)
    TextView mGameGoldOneTv;

    @BindView(R.id.tv_game_gold_two)
    TextView mGameGoldTwoTv;

    @BindView(R.id.tv_game_gold_three)
    TextView mGameGoldThreeTv;

    @BindView(R.id.tv_game_gold_four)
    TextView mGameGoldFourTv;

    @BindView(R.id.tv_user_money)
    TextView mUserMoneyTv;

    @BindView(R.id.layout_task_info)
    FrameLayout mTaskInfoLayout;

    //广告配置
    private TTAdNative mTTAdNative;

    private TTAdDislike mTTAdDislike;

    private TTNativeExpressAd mTTAd;

    private TTRewardVideoAd mttRewardVideoAd;

    private View adView;

    UserInfoPresenterImp userInfoPresenterImp;

    InitInfoPresenterImp initInfoPresenterImp;

    private TakeGoldInfoPresenterImp takeGoldInfoPresenterImp;

    private int exChange = 100;

    private TextView[] goldTxs;

    PermissionDialog permissionDialog;

    HongBaoDialog hongBaoDialog;

    LoginDialog loginDialog;

    private UMShareAPI mShareAPI = null;

    private ProgressDialog progressDialog = null;

    ReceiveGoldDialog receiveGoldDialog;

    ReceiveDoubleGoldDialog receiveDoubleGoldDialog;

    private int clickIndex = 0;

    public static int COUNT_SPACE = 1 * 60;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mTaskInfoLayout.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_game;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        GameActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    void showReadPhone() {
        GameActivityPermissionsDispatcher.showReadLocationWithPermissionCheck(this);
    }

    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    void onReadPhoneDenied() {
        //Toast.makeText(this, R.string.permission_storage_denied, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        }
    }

    @OnShowRationale(Manifest.permission.READ_PHONE_STATE)
    void showRationaleForReadPhone(PermissionRequest request) {
        //Toast.makeText(this, R.string.permission_read_phone_rationale, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        } else {
            request.proceed();
        }
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    void onReadPhoneNeverAskAgain() {
        //Toast.makeText(this, R.string.permission_storage_never_ask_again, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        }
    }

    //读取地理位置权限
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showReadLocation() {
        readPhoneTask();
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onReadLocationDenied() {
        //Toast.makeText(this, R.string.permission_storage_denied, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        }
    }

    @OnShowRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showRationaleForReadLocation(PermissionRequest request) {
        //Toast.makeText(this, R.string.permission_read_phone_rationale, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        } else {
            request.proceed();
        }
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onReadLocationNeverAskAgain() {
        //Toast.makeText(this, R.string.permission_storage_never_ask_again, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        }
    }

    public void readPhoneTask() {
        Logger.i("readPhoneTask--->" + PhoneUtils.getIMEI());
        userInfoPresenterImp.imeiLogin(PhoneUtils.getIMEI(), App.agentId, "1", 0);
    }

    @Override
    protected void initVars() {

        List<Animator> animators = new ArrayList<>();
        ObjectAnimator translationYAnim1 = ObjectAnimator.ofFloat(mGameGoldOneTv, "translationY", -12.0f, 12.0f, -12.0f);
        translationYAnim1.setDuration(2500);
        translationYAnim1.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim1.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim1);

        ObjectAnimator translationYAnim2 = ObjectAnimator.ofFloat(mGameGoldOneTv, "translationY", -12.0f, 12.0f, -12.0f);
        translationYAnim2.setDuration(2500);
        translationYAnim2.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim2.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim2);

        ObjectAnimator translationYAnim3 = ObjectAnimator.ofFloat(mGameGoldThreeTv, "translationY", -12.0f, 12.0f, -12.0f);
        translationYAnim3.setDuration(2500);
        translationYAnim3.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim3.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim3);

        ObjectAnimator translationYAnim4 = ObjectAnimator.ofFloat(mGameGoldFourTv, "translationY", -12.0f, 12.0f, -12.0f);
        translationYAnim4.setDuration(2500);
        translationYAnim4.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim4.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim4);

        AnimatorSet btnSexAnimatorSet = new AnimatorSet();
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.setStartDelay(100);
        btnSexAnimatorSet.start();
    }

    @Override
    protected void initViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录");
        mShareAPI = UMShareAPI.get(this);

        hongBaoDialog = new HongBaoDialog(this, R.style.common_dialog);
        permissionDialog = new PermissionDialog(this, R.style.common_dialog);
        permissionDialog.setPermissionConfigListener(this);
        loginDialog = new LoginDialog(this, R.style.common_dialog);
        loginDialog.setLoginListener(this);

        receiveGoldDialog = new ReceiveGoldDialog(this, R.style.gold_dialog);
        receiveGoldDialog.setGoldDialogListener(this);

        receiveDoubleGoldDialog = new ReceiveDoubleGoldDialog(this, R.style.gold_dialog);
        receiveDoubleGoldDialog.setGoldDoubleDialogListener(this);

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        loadVideoAd(Constants.VIDEO_CODE_ID, TTAdConstant.VERTICAL);

        goldTxs = new TextView[]{mGameGoldOneTv, mGameGoldTwoTv, mGameGoldThreeTv};

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        YcGameWebView ycGameWebView = new YcGameWebView(this);
        mGameLayout.addView(ycGameWebView, params);

        YcGameSDK.getInstance().initGame(this, "yc_game", new YcGameInitCallback() {
            @Override
            public void gameInit() {
                YcGameInfo ycGameInfo = YcGameSDK.getInstance().getGameInfoByGameId("2048ball");
                if (ycGameInfo != null) {
                    if (ycGameInfo.getDirection() == 1) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    }
                    Log.d(TAG, "游戏url:" + ycGameInfo.getUrl());

                    ycGameWebView.post(new Runnable() {
                        @Override
                        public void run() {
                            ycGameWebView.loadUrl(ycGameInfo.getUrl());
                        }
                    });
                    return;
                } else {
                    Log.d(TAG, "游戏信息未获取");
                }
            }
        });

        YcGameSDK.getInstance().setGameAdShowCallback(this);
        YcGameSDK.getInstance().setGameClickCallback(this);
        YcGameSDK.getInstance().setGameDataCallback(this);
        YcGameSDK.getInstance().setGamePlayTimeCallback(this);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        }, 8000);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userInfoPresenterImp = new UserInfoPresenterImp(this, this);
        initInfoPresenterImp = new InitInfoPresenterImp(this, this);
        takeGoldInfoPresenterImp = new TakeGoldInfoPresenterImp(this, this);

        loadExpressAd(Constants.BANNER_CODE_ID);
        GameActivityPermissionsDispatcher.showReadPhoneWithPermissionCheck(this);
        SPUtils.getInstance().put("one_gold_get_date", 0L);
        fourGoldShow();
    }

    @Override
    public void gameClickCallback(String gameName, String gameID) {
        Log.d(TAG, "游戏点击:(" + gameName + "," + gameID + ")");
        //Toast.makeText(this, "游戏点击:(" + gameName + "," + gameID + ")", Toast.LENGTH_LONG).show();
    }

    public int showedGold = 0;
    public int oneCurrentGoldNum;
    public int twoCurrentGoldNum;
    public int threeCurrentGoldNum;
    public int fourCurrentGoldNum;

    @Override
    public void gameDataCallback(String type, String num) {
        Log.d(TAG, "游戏数据:(" + type + "," + num + ")");
        //Toast.makeText(this, "游戏数据:(" + type + "," + num + ")", Toast.LENGTH_LONG).show();

        if (!StringUtils.isEmpty(num)) {
            int tempNum = Integer.parseInt(num);
            int goldNum = (tempNum - showedGold) / exChange;
            Logger.i("goldNum--->" + goldNum);
            if (goldNum >= 1) {
                for (int i = 0; i < goldTxs.length; i++) {
                    if (goldTxs[i].getVisibility() == View.GONE) {

                        showedGold = tempNum;

                        if (i == 0) {
                            oneCurrentGoldNum = RandomUtils.nextInt(BUBBLE_START, BUBBLE_END);
                        }

                        if (i == 1) {
                            twoCurrentGoldNum = RandomUtils.nextInt(BUBBLE_START, BUBBLE_END);
                        }

                        if (i == 2) {
                            threeCurrentGoldNum = RandomUtils.nextInt(BUBBLE_START, BUBBLE_END);
                        }

                        Logger.i("CurrentGoldNum--->" + oneCurrentGoldNum + "---" + twoCurrentGoldNum + "---" + threeCurrentGoldNum + "---" + fourCurrentGoldNum);
                        randomShowGold(goldTxs[i], 0);

                        break;
                    }
                }
            }
        }
    }

    private CountDownTimer oneGoldTimer;

    void fourGoldShow() {

        long diffSecond = 0;
        if (SPUtils.getInstance().getLong("one_gold_get_date", 0L) > 0) {
            long tempDate = SPUtils.getInstance().getLong("one_gold_get_date");
            diffSecond = TimeUtils.getTimeSpanByNow(tempDate + COUNT_SPACE * 1000, TimeConstants.SEC);
        } else {
            diffSecond = 0;
            long dateNow = TimeUtils.getNowMills();
            SPUtils.getInstance().put("one_gold_get_date", dateNow);
        }

        long dateNow = TimeUtils.getNowMills();
        SPUtils.getInstance().put("one_gold_get_date", dateNow);

        Logger.i("间隔的时间--->" + diffSecond);

        if (diffSecond == 0) {
            mGameGoldFourTv.setVisibility(View.GONE);
            long tempDate = SPUtils.getInstance().getLong("one_gold_get_date");
            diffSecond = TimeUtils.getTimeSpanByNow(tempDate + COUNT_SPACE * 1000, TimeConstants.SEC);
            //翻倍看视频
            oneGoldTimer = new CountDownTimer(diffSecond * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Logger.i("one剩余时间--->" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    long dateNow = TimeUtils.getNowMills();
                    SPUtils.getInstance().put("one_gold_get_date", dateNow);
                    mGameGoldFourTv.setVisibility(View.VISIBLE);
                }
            }.start();
        } else {
            mGameGoldFourTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void gamePlayTimeCallback(String gameId, int playTimeInSeconds) {
        Log.d(TAG, "游戏时间:(" + gameId + "," + playTimeInSeconds + ")");
        //Toast.makeText(this, "游戏时间:(" + gameId + "," + playTimeInSeconds + ")", Toast.LENGTH_LONG).show();
    }

    @Override
    public void gameAdShow(YcGameAdCallback callback) {
        //播放视频完成后回调
        ycGameAdCallback = callback;
        //Log.d(TAG, "显示视频");
        //Toast.makeText(this, "显示视频", Toast.LENGTH_LONG).show();
        mGameLayout.post(new Runnable() {
            @Override
            public void run() {
                seeVideo();
            }
        });
    }

    public void randomShowGold(TextView showGoldTv, int goldNum) {
        showGoldTv.post(new Runnable() {
            @Override
            public void run() {
                //showGoldTv.setText(goldNum + "");
                showGoldTv.setVisibility(View.VISIBLE);
            }
        });
    }

    public void seeVideo() {
        //ToastUtils.showLong("模拟视频看完，领取金币成功");
        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(this, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "home_video_ad");
            //mttRewardVideoAd = null;
        } else {
            Logger.i("请先加载广告");
            loadVideoAd(Constants.VIDEO_CODE_ID, TTAdConstant.VERTICAL);
        }
    }

    @OnClick(R.id.tv_game_gold_one)
    void playVideo() {
        //seeVideo();
        clickIndex = 0;
        mGameGoldOneTv.setVisibility(View.GONE);
        takeGoldNum(3, "1", oneCurrentGoldNum + "", 0);

    }

    @OnClick(R.id.tv_game_gold_two)
    void playTwoVideo() {
        clickIndex = 1;
        mGameGoldTwoTv.setVisibility(View.GONE);
        takeGoldNum(3, "1", twoCurrentGoldNum + "", 0);
    }

    @OnClick(R.id.tv_game_gold_three)
    void threeVideo() {
        clickIndex = 2;
        mGameGoldThreeTv.setVisibility(View.GONE);
        takeGoldNum(3, "1", threeCurrentGoldNum + "", 0);
    }

    @OnClick(R.id.tv_game_gold_four)
    void fourVideo() {
        clickIndex = 3;
        mGameGoldFourTv.setVisibility(View.GONE);
        fourCurrentGoldNum = RandomUtils.nextInt(BUBBLE_START, BUBBLE_END);
        fourGoldShow();
        seeVideo();
    }

    /**
     * 领取金币
     *
     * @param type
     */
    public void takeGoldNum(int type, String taskId, String gold, int state) {

        try {
            TakeGoldInfo takeGoldInfo = new TakeGoldInfo();
            takeGoldInfo.setTaskId(taskId);
            takeGoldInfo.setUserId(App.mUserInfo != null ? App.mUserInfo.getId() : "");
            takeGoldInfo.setGold(Integer.parseInt(gold));

            if (type == 3) {
                takeGoldInfo.setIsDouble(state);
                takeGoldInfoPresenterImp.takeLuckGold(takeGoldInfo);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private long startTime = 0;

    private boolean mHasShowDownloadActive = false;

    YcGameAdCallback ycGameAdCallback;

    //广告的加载
    private void loadExpressAd(String codeId) {
        if (adView != null) {
            adView = null;
        }
        //mExpressContainer.removeAllViews();
        float expressViewWidth = 300;
        float expressViewHeight = 145;

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
                if (adView != null) {
                    adView = null;
                }
                //mExpressContainer.removeAllViews();
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
                if (adView != null) {
                    adView = null;
                }

                adView = view;
                //mExpressContainer.removeAllViews();
                //mExpressContainer.addView(view);
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

            final DislikeDialog dislikeDialog = new DislikeDialog(GameActivity.this, words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    Logger.i("点击 " + filterWord.getName());
                    //用户选择不喜欢原因后，移除广告展示
                    //mExpressContainer.removeAllViews();
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(GameActivity.this, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Logger.i("点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                //mExpressContainer.removeAllViews();
            }

            @Override
            public void onCancel() {
                Logger.i("点击取消 ");
            }
        });
    }

    private void loadVideoAd(String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setAdCount(1)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(1)  //奖励的数量
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("code" + code + "---" + message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                Logger.i("rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                Logger.i("rewardVideoAd loaded");
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Logger.i("rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Logger.i("rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Logger.i("rewardVideoAd close");
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        Logger.i("rewardVideoAd complete");
                        //缓存下一个视频
                        loadVideoAd(Constants.VIDEO_CODE_ID, TTAdConstant.VERTICAL);
                    }

                    @Override
                    public void onVideoError() {
                        Logger.i("rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        Logger.i("verify:" + rewardVerify + " amount:" + rewardAmount + " name:" + rewardName);
                        if (ycGameAdCallback != null) {
                            ycGameAdCallback.gameAdSuccess();
                            ycGameAdCallback = null;
                        } else {
                            String resGold = "";
                            if (clickIndex == 1) {
                                resGold = twoCurrentGoldNum + "";
                            }
                            if (clickIndex == 3) {
                                SPUtils.getInstance().put("one_gold_get_date", 0L);
                                fourGoldShow();
                                resGold = fourCurrentGoldNum * 2 + "";
                            }
                            takeGoldNum(3, "1", resGold, 1);
                        }
                    }

                    @Override
                    public void onSkippedVideo() {
                        Logger.i("rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            Logger.i("下载中，点击下载区域暂停");
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        Logger.i("下载暂停，点击下载区域继续");
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        Logger.i("下载失败，点击下载区域重新下载");
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        Logger.i("下载完成，点击下载区域重新下载");
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        Logger.i("安装完成，点击下载区域打开");
                    }
                });
            }
        });
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
            if (tData instanceof UserInfoRet && ((UserInfoRet) tData).getCode() == Constants.SUCCESS) {
                Logger.i("user info --->" + JSON.toJSONString(tData));
                if (((UserInfoRet) tData).getData() != null) {
                    if (!StringUtils.isEmpty(((UserInfoRet) tData).getMsg())) {
                        Toasty.normal(this, ((UserInfoRet) tData).getMsg()).show();
                        //存储用户信息
                        SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(((UserInfoRet) tData).getData()));
                        SPUtils.getInstance().put(Constants.LOCAL_LOGIN, true);
                        App.isLogin = true;
                    }

                    App.mUserInfo = ((UserInfoRet) tData).getData();
                    mUserMoneyTv.setText(App.mUserInfo != null ? MatrixUtils.getPrecisionMoney(App.mUserInfo.getAmount()) + "元" : "");
                    initInfoPresenterImp.initInfo(((UserInfoRet) tData).getData().getId());
                }
            }

            if (tData instanceof InitInfoRet && ((InitInfoRet) tData).getCode() == Constants.SUCCESS) {
                Logger.i("init info--->" + JSON.toJSONString(tData));
                if (((InitInfoRet) tData).getData() != null) {
                    App.initInfo = ((InitInfoRet) tData).getData();
                    App.userTodayStep = App.initInfo.getUserStepData().getStepNum();
                    Logger.i("user today step--->" + App.userTodayStep);
                }

                MessageEvent messageEvent = new MessageEvent("init_success");
                EventBus.getDefault().post(messageEvent);

                if (hongBaoDialog != null && !hongBaoDialog.isShowing()) {
                    if (App.mUserInfo.getShowNew() == 1) {
                        hongBaoDialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hongBaoDialog.autoOpenHongBao(App.initInfo.getNewTaskConfig().getGold());
                            }
                        }, 2000);
                    }
                }
            }

            if (tData instanceof TakeGoldInfoRet) {
                Logger.i("take gold info--->" + JSON.toJSONString(tData));
                if (((TakeGoldInfoRet) tData).getCode() == Constants.SUCCESS) {
                    if (((TakeGoldInfoRet) tData).getData() != null) {
                        TakeGoldInfo takeGoldInfo = ((TakeGoldInfoRet) tData).getData();
                        mUserMoneyTv.setText(MatrixUtils.getPrecisionMoney(takeGoldInfo.getAmount()) + "元");
                        //不可翻倍
                        if (receiveGoldDialog == null) {
                            receiveGoldDialog = new ReceiveGoldDialog(GameActivity.this, R.style.gold_dialog);
                            receiveGoldDialog.setGoldDialogListener(this);
                        }
                        receiveGoldDialog.show();

                        String currentTakeGold = "";
                        if (clickIndex == 0) {
                            currentTakeGold = oneCurrentGoldNum + "";
                        }
                        if (clickIndex == 1) {
                            currentTakeGold = twoCurrentGoldNum + "";
                        }
                        if (clickIndex == 2) {
                            currentTakeGold = threeCurrentGoldNum + "";
                        }
                        if (clickIndex == 3) {
                            currentTakeGold = fourCurrentGoldNum  * 2 + "";
                        }

                        receiveGoldDialog.updateGoldInfo(currentTakeGold, takeGoldInfo.getGold() + "", "≈" + MatrixUtils.getPrecisionMoney(takeGoldInfo.getAmount()) + "元", adView);

                        loadExpressAd(Constants.BANNER_CODE_ID);
                        //TODO
                        //每次领取金币后同步一次最新的步数
                        //sendStepToServer(currentStepNum);
                    } else {
                        ToastUtils.showLong(((TakeGoldInfoRet) tData).getMsg());
                    }
                } else {

                    ToastUtils.showLong(((TakeGoldInfoRet) tData).getMsg());
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

    @OnClick(R.id.iv_task)
    void taskList() {
        if (App.mUserInfo == null || !SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false)) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
        } else {
            Intent intent = new Intent(this, TaskActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.layout_hongbao)
    void goToHongBao() {
        if (App.mUserInfo == null || !SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false)) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
        } else {
            Intent intent = new Intent(this, CashActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void grantPermission() {
        GameActivityPermissionsDispatcher.showReadPhoneWithPermissionCheck(this);
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
            Toasty.normal(GameActivity.this, "授权失败").show();
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
            Toasty.normal(GameActivity.this, "授权取消").show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    @Override
    public void wxLogin() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
        mShareAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, authListener);
    }

    @Override
    public void phoneLogin() {
        Intent intent = new Intent(this, PhoneLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void closeDoubleGoldDialog() {
        if (receiveDoubleGoldDialog != null && receiveDoubleGoldDialog.isShowing()) {
            receiveDoubleGoldDialog.dismiss();
            receiveDoubleGoldDialog = null;
        }

        //关闭广告弹窗时，移除adView
        if (adView != null && adView.getParent() != null) {
            ((ViewGroup) adView.getParent()).removeView(adView);
        }
    }

    @Override
    public void clickDoubleGold() {
        closeDoubleGoldDialog();

        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(this, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "home_video_ad");
            //mttRewardVideoAd = null;
        } else {
            Logger.i("请先加载广告");
        }
    }

    @Override
    public void closeGoldDialog() {
        if (receiveGoldDialog != null && receiveGoldDialog.isShowing()) {
            receiveGoldDialog.dismiss();
            receiveGoldDialog = null;
        }

        //关闭广告弹窗时，移除adView
        if (adView != null && adView.getParent() != null) {
            ((ViewGroup) adView.getParent()).removeView(adView);
        }
    }
}
