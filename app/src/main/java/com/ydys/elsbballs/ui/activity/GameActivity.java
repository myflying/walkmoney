package com.ydys.elsbballs.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.orhanobut.logger.Logger;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.ss.android.common.applog.TeaAgent;
import com.ss.android.common.applog.TeaConfigBuilder;
import com.ss.android.common.lib.AppLogNewUtils;
import com.ss.android.common.lib.EventUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.h5.core.YCGameClickCallback;
import com.yc.h5.core.YcGameAdCallback;
import com.yc.h5.core.YcGameAdShowCallback;
import com.yc.h5.core.YcGameDataCallback;
import com.yc.h5.core.YcGameInitCallback;
import com.yc.h5.core.YcGamePlayTimeCallback;
import com.yc.h5.core.YcGameSDK;
import com.yc.h5.core.YcGameWebView;
import com.ydys.elsbballs.App;
import com.ydys.elsbballs.R;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.GameTimeInfoRet;
import com.ydys.elsbballs.bean.HomeDataInfo;
import com.ydys.elsbballs.bean.HomeDateInfoRet;
import com.ydys.elsbballs.bean.InitInfo;
import com.ydys.elsbballs.bean.InitInfoRet;
import com.ydys.elsbballs.bean.TakeGoldInfo;
import com.ydys.elsbballs.bean.TakeGoldInfoRet;
import com.ydys.elsbballs.bean.UserInfoRet;
import com.ydys.elsbballs.common.Constants;
import com.ydys.elsbballs.presenter.GameTimeInfoPresenterImp;
import com.ydys.elsbballs.presenter.HomeDataInfoPresenterImp;
import com.ydys.elsbballs.presenter.InitInfoPresenterImp;
import com.ydys.elsbballs.presenter.LogInfoPresenterImp;
import com.ydys.elsbballs.presenter.NewUserMoneyPresenterImp;
import com.ydys.elsbballs.presenter.Presenter;
import com.ydys.elsbballs.presenter.ReportInfoPresenterImp;
import com.ydys.elsbballs.presenter.TakeGoldInfoPresenterImp;
import com.ydys.elsbballs.presenter.UserInfoPresenterImp;
import com.ydys.elsbballs.ui.custom.CustomRotateAnim;
import com.ydys.elsbballs.ui.custom.GameRuleDialog;
import com.ydys.elsbballs.ui.custom.GoldWireLayout;
import com.ydys.elsbballs.ui.custom.HongBaoDialog;
import com.ydys.elsbballs.ui.custom.LoadDialog;
import com.ydys.elsbballs.ui.custom.LoginDialog;
import com.ydys.elsbballs.ui.custom.PermissionDialog;
import com.ydys.elsbballs.ui.custom.ReceiveDoubleGoldDialog;
import com.ydys.elsbballs.ui.custom.ReceiveGoldDialog;
import com.ydys.elsbballs.util.MatrixUtils;
import com.ydys.elsbballs.util.MediaHelper;
import com.ydys.elsbballs.util.RandomUtils;
import com.ydys.elsbballs.util.TTAdManagerHolder;
import com.ydys.elsbballs.util.WeakHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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
public class GameActivity extends BaseActivity implements YCGameClickCallback, YcGameDataCallback, YcGamePlayTimeCallback, YcGameAdShowCallback, IBaseView, PermissionDialog.PermissionConfigListener, LoginDialog.LoginListener, ReceiveGoldDialog.GoldDialogListener, ReceiveDoubleGoldDialog.GoldDoubleDialogListener, HongBaoDialog.OpenHBListener, GameRuleDialog.GameRuleListener, NetworkUtils.OnNetworkStatusChangedListener, WeakHandler.IHandler {

    public static String TAG = "GameFragment";

    public static int BUBBLE_START = 1000;

    public static int BUBBLE_END = 2000;

    @BindView(R.id.layout_content)
    FrameLayout mContentLayout;

    @BindView(R.id.layout_tool_nav)
    FrameLayout mTopToolLayout;

    @BindView(R.id.layout_top_func)
    LinearLayout mTopFuncLayout;

    @BindView(R.id.layout_game_content)
    FrameLayout mGameLayout;

    @BindView(R.id.banner_express_container)
    FrameLayout mBannerExpressContainer;

    @BindView(R.id.layout_one_gold)
    FrameLayout oneGoldLayout;

    @BindView(R.id.iv_one_gold)
    ImageView mOneGoldIv;

    @BindView(R.id.layout_two_gold)
    FrameLayout twoGoldLayout;

    @BindView(R.id.iv_two_gold)
    ImageView mTwoGoldIv;

//    @BindView(R.id.tv_game_gold_two)
//    TextView mGameGoldTwoTv;

    @BindView(R.id.tv_game_gold_three)
    TextView mGameGoldThreeTv;

    @BindView(R.id.tv_game_gold_four)
    TextView mGameGoldFourTv;

    @BindView(R.id.layout_user_money)
    FrameLayout mUserMoneyLayout;

    @BindView(R.id.iv_task_icon)
    ImageView mTaskIv;

    @BindView(R.id.layout_gold_detail)
    FrameLayout mGoldDetailLayout;

    @BindView(R.id.tv_user_game_gold_num)
    TickerView mUserGoldNumTv;

    @BindView(R.id.tv_user_amount)
    TextView mUserAmountNumTv;

    @BindView(R.id.iv_user_cash)
    ImageView mUserCashIv;

    @BindView(R.id.iv_game_rule)
    ImageView mGameRuleIv;

    @BindView(R.id.tv_gold_unit)
    TextView mGoldUnitTv;

    @BindView(R.id.iv_cash_point)
    ImageView mCashPointIv;

    @BindView(R.id.iv_task_point)
    ImageView mTaskPointIv;

    //广告配置
    private TTAdNative mTTAdNative;

    private TTAdDislike mTTAdDislike;

    private TTNativeExpressAd mTTAd;

    private TTRewardVideoAd mttRewardVideoAd;

    private TTRewardVideoAd mGameVideoAd;

    private TTNativeExpressAd mBannerTTAd;

    private View adView;

    UserInfoPresenterImp userInfoPresenterImp;

    InitInfoPresenterImp initInfoPresenterImp;

    private HomeDataInfoPresenterImp homeDataInfoPresenterImp;

    private TakeGoldInfoPresenterImp takeGoldInfoPresenterImp;

    private GameTimeInfoPresenterImp gameTimeInfoPresenterImp;

    private ReportInfoPresenterImp reportInfoPresenterImp;

    private NewUserMoneyPresenterImp newUserMoneyPresenterImp;

    private LogInfoPresenterImp logInfoPresenterImp;

    private int exChange = 100;

    PermissionDialog permissionDialog;

    HongBaoDialog hongBaoDialog;

    LoginDialog loginDialog;

    private UMShareAPI mShareAPI = null;

    private ProgressDialog progressDialog = null;

    ReceiveGoldDialog receiveGoldDialog;

    ReceiveDoubleGoldDialog receiveDoubleGoldDialog;

    private int clickIndex = 0;

    private int isDouble;//是否是翻倍金币的操作

    public static int COUNT_SPACE = 2 * 60;

    public static int ONE_COUNT_SPACE = 1 * 60;

    public static int TWO_COUNT_SPACE = 2 * 60;

    public static int THREE_COUNT_SPACE = 3 * 60;

    private CountDownTimer fourGoldTimer;

    private CountDownTimer oneGoldTimer;

    private CountDownTimer twoGoldTimer;

    private boolean isRuning;//金币掉落动画是否正在进行

    private TimerTask mGoldTask;

    private Timer timer;

    public static int COUNT = 10;

    private int count;

    private String todayDate;

    private HomeDataInfo homeDataInfo;

    GameRuleDialog gameRuleDialog;

    private boolean isFirstLoad = true;

    private int dialogType = 1;

    private boolean isFirstStartCount = true;

    public int FLOAT_SHOW_NUM = 10;

    private boolean isReloadVideo;

    private LoadDialog loadDialog;

    private String spaCodeId;

    private String homeBannerCodeId;

    private String windowCodeId;

    private String windowVideoCodeId;

    private String restartVideoCodeId;

    private boolean spaIsShow;

    private boolean spaIsClick;

    private boolean spaIsDowned;

    private boolean spaIsInstall;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //游戏底部banner
                    loadBannerExpressAd(homeBannerCodeId);

                    mUserGoldNumTv.setCharacterLists(TickerUtils.provideNumberList());
                    mUserGoldNumTv.setText("0");
                    GameActivityPermissionsDispatcher.showReadPhoneWithPermissionCheck(GameActivity.this);
                    mTopFuncLayout.setVisibility(View.VISIBLE);
                    //mTopFuncLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(90)));
                    //mTaskInfoLayout.setVisibility(View.VISIBLE);
                    //showHB();
                    break;
                case 2:
                    //得分后开始倒计时
                    oneGoldShow();
                    twoGoldShow();
                    if (App.mUserInfo != null) {
                        reportInfoPresenterImp.startPlayGame(App.mUserInfo.getId());
                    }
                    break;
                default:
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
        GameActivityPermissionsDispatcher.showReadStorageWithPermissionCheck(this);
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

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showReadStorage() {
        GameActivityPermissionsDispatcher.showReadLocationWithPermissionCheck(this);
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onReadStorageDenied() {
        //Toast.makeText(this, R.string.permission_storage_denied, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        }
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForReadStorage(PermissionRequest request) {
        //Toast.makeText(this, R.string.permission_read_phone_rationale, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        } else {
            request.proceed();
        }
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onReadStorageNeverAskAgain() {
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

        TeaAgent.init(TeaConfigBuilder.create(GameActivity.this)
                .setAppName("youdianyisi2048")
                .setChannel(App.agentId)
                .setAid(173542)
                .createTeaConfig());

        loadExpressAd(windowCodeId);
        loadVideoAd(windowVideoCodeId, TTAdConstant.VERTICAL);

        loadGameVideoAd(restartVideoCodeId, TTAdConstant.VERTICAL);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                EventUtils.setRegister("imei_code", true);

                Logger.i("手机串号注册接口--->" + PhoneUtils.getIMEI() + "--->" + App.agentId);
                userInfoPresenterImp.imeiLogin(PhoneUtils.getIMEI(), App.agentId, App.softId, App.appName);
            }
        }, 10);

        //showGuideView();
    }

    public void showGuideView() {
//        NewbieGuide.with(this)
//                .setLabel("guide1")
//                .alwaysShow(true)//总是显示，调试时可以打开
//                .addGuidePage(GuidePage.newInstance().setLayoutRes(R.layout.guide_view_one))
//                .setOnGuideChangedListener(new OnGuideChangedListener() {
//                    @Override
//                    public void onShowed(Controller controller) {
//                        Toast.makeText(GameActivity.this, "引导层显示", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onRemoved(Controller controller) {
//                        Toast.makeText(GameActivity.this, "引导层消失", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .show();

        NewbieGuide.with(this)
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .setLayoutRes(R.layout.guide_view_one)//设置引导页布局
                                .setEverywhereCancelable(true)
                )
                .addGuidePage(//添加一页引导页
                        GuidePage.newInstance()//创建一个实例
                                .setLayoutRes(R.layout.guide_view_two)//设置引导页布局
                                .setEverywhereCancelable(true)
                ).show();

    }

    @Override
    protected void initVars() {
        NetworkUtils.registerNetworkStatusChangedListener(this);

        /*List<Animator> animators = new ArrayList<>();
        ObjectAnimator translationYAnim1 = ObjectAnimator.ofFloat(mGameGoldOneTv, "translationY", -12.0f, 12.0f, -12.0f);
        translationYAnim1.setDuration(2500);
        translationYAnim1.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim1.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim1);

        ObjectAnimator translationYAnim2 = ObjectAnimator.ofFloat(mGameGoldTwoTv, "translationY", -12.0f, 12.0f, -12.0f);
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
        btnSexAnimatorSet.start();*/

        spaCodeId = SPUtils.getInstance().getString("spa_code_id", Constants.SPA_CODE_ID);
        homeBannerCodeId = SPUtils.getInstance().getString("home_banner_code_id", Constants.HOME_BANNER_ID);
        windowCodeId = SPUtils.getInstance().getString("window_code_id", Constants.BANNER_CODE_ID);
        windowVideoCodeId = SPUtils.getInstance().getString("window_video_code_id", Constants.VIDEO_CODE_ID);
        restartVideoCodeId = SPUtils.getInstance().getString("restart_video_code_id", Constants.GAME_VIDEO_CODE_ID);

        showAnimation(mOneGoldIv);
        showAnimation(mTwoGoldIv);
    }

    private void showAnimation(ImageView goldIv) {
        // 获取自定义动画实例
        CustomRotateAnim rotateAnim = CustomRotateAnim.getCustomRotateAnim();
        // 一次动画执行1秒
        rotateAnim.setDuration(1800);
        // 设置为循环播放
        rotateAnim.setRepeatCount(-1);
        // 设置为匀速
        rotateAnim.setInterpolator(new LinearInterpolator());
        // 开始播放动画
        goldIv.startAnimation(rotateAnim);
    }

    protected void hideBottomMenu() {
        //隐藏虚拟按键
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            if (v != null) {
                v.setSystemUiVisibility(View.GONE);
            }
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            if (decorView != null) {
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    @Override
    protected void initViews() {
        mShareAPI = UMShareAPI.get(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录");

        loadDialog = new LoadDialog(this, R.style.common_dialog);
        gameRuleDialog = new GameRuleDialog(this, R.style.common_dialog);
        gameRuleDialog.setGameRuleListener(this);

        hongBaoDialog = new HongBaoDialog(this, R.style.common_dialog);
        hongBaoDialog.setOpenHBListener(this);

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

        adHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT);
        //加载开屏广告
        loadSplashAd();

        YcGameWebView ycGameWebView = new YcGameWebView(this);
        //7.0以下手机设置游戏固定高度
        if (Build.VERSION.SDK_INT < 24) {
            mTopToolLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(70)));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(630));
            mGameLayout.addView(ycGameWebView, params);
        } else {
            mTopToolLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(90)));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mGameLayout.addView(ycGameWebView, params);
        }

        YcGameSDK.getInstance().initGame(this, "yc_game", new YcGameInitCallback() {
            @Override
            public void gameInit() {
                ycGameWebView.post(new Runnable() {
                    @Override
                    public void run() {
                        ycGameWebView.loadUrl("http://localhost:12568/web-mobile/index.html");
                    }
                });
            }
        });

        YcGameSDK.getInstance().setGameAdShowCallback(this);
        YcGameSDK.getInstance().setGameClickCallback(this);
        YcGameSDK.getInstance().setGameDataCallback(this);
        YcGameSDK.getInstance().setGamePlayTimeCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i("onresume--->");
        //观看过视频，则引导用户去提现
        if (App.mUserInfo != null && App.mUserInfo.getNewUserTx() == 0) {
            if (SPUtils.getInstance().getBoolean("is_show_cash_point", false)) {
                mCashPointIv.setVisibility(View.VISIBLE);
            } else {
                mCashPointIv.setVisibility(View.GONE);
            }
        } else {
            mCashPointIv.setVisibility(View.GONE);
        }
        hideBottomMenu();

        //判断是否该跳转到主页面
        if (mForceGoMain) {
            mHandler.removeCallbacksAndMessages(null);
            splashAdFinish();
        }
    }

    //根据返回值来控制首页的红包是否显示
    void showHB() {
        //新人红包
        if (App.mUserInfo != null && App.mUserInfo.getNewUserTaskConfig().getNewUserGold() == 0) {
            if (hongBaoDialog != null && !hongBaoDialog.isShowing()) {
                hongBaoDialog.show();
                clickIndex = 4;
                hongBaoDialog.updateDialogInfo(1, App.mUserInfo.getNewUserTaskConfig().getMoney() + "");

//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        hongBaoDialog.autoOpenHongBao(App.initInfo != null ? App.initInfo.getNewTaskConfig().getGold() : 0);
//                    }
//                }, 1500);
            }
        }

        //登录红包
        if (App.mUserInfo != null && App.mUserInfo.getLoginTaskConfig().getLoginTaskGold() == 0) {
            if (hongBaoDialog != null && !hongBaoDialog.isShowing() && !SPUtils.getInstance().getBoolean(todayDate + "_show_hongbao", false)) {
                SPUtils.getInstance().put(todayDate + "_show_hongbao", true);
                hongBaoDialog.show();
                clickIndex = 5;
                hongBaoDialog.updateDialogInfo(2, "0.3");
            }
        }
    }

    //缩放用户收益对应的textView
    public void scaleUserProfit(TickerView showTv) {
        showTv.setCharacterLists(TickerUtils.provideNumberList());
        showTv.setAnimationDuration(1200);
        showTv.setAnimationInterpolator(new OvershootInterpolator());
        showTv.setGravity(Gravity.START);

        String totalGold = App.mUserInfo.getGold() + "";
        if (App.mUserInfo != null) {
            if (App.mUserInfo.getGold() > 100000) {
                double total = (double) App.mUserInfo.getGold();
                double last = total / 10000d;
                totalGold = (int) last + "";
                mGoldUnitTv.setVisibility(View.VISIBLE);
            } else {
                mGoldUnitTv.setVisibility(View.GONE);
            }
        }

        showTv.setText(totalGold);

        //播放得到金币的声音
        try {
            AssetFileDescriptor fd = getAssets().openFd("gold_sound.mp3");
            MediaHelper.playSound(fd, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //ToastUtils.showLong("播放完成");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始执行金币领取后的动画
     */
    private void startGoldTask() {
        /**
         * 中奖金币掉落场景位置
         * 起始位置X：在礼物Item X轴中间位置向右偏移50DP之间
         * 起始位置Y：在礼物Item高度中心位置
         * 中间位置X：向起始位置X轴正数偏移200像素
         * 中间位置Y：向起始位置Y轴负数偏移300像素
         * 终点位置X：礼物面板按钮中心-->顶部边缘之间
         * 终点位置Y：礼物面板按钮中心位置
         */
        if (isRuning) return;
        count = 0;
        isRuning = true;
        mGoldTask = new TimerTask() {
            @Override
            public void run() {

                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                rootView.post(new Runnable() {
                    @Override
                    public void run() {
                        Context context = getContext();
                        if (null != context) {
                            GoldWireLayout goldWireLayout = new GoldWireLayout(getContext());
                            int startX = ScreenUtils.getScreenWidth() / 2 + RandomUtils.nextInt(5, 50);
                            int startY = SizeUtils.dp2px(200) + RandomUtils.nextInt(5, 50);
                            goldWireLayout.setStartPosition(new Point(startX, startY));
                            ViewGroup rootView = (ViewGroup) ((Activity) context).getWindow().getDecorView();
                            rootView.addView(goldWireLayout);
                            goldWireLayout.setEndPosition(new Point(65, 105));
                            goldWireLayout.startBeizerAnimation();
                            count++;
                            if (count >= COUNT) {
                                stopTask();
                            }
                        }
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(mGoldTask, 0, 100);
    }

    private void stopTask() {
        if (null != timer) timer.cancel();
        timer = null;
        mGoldTask = null;
        isRuning = false;
        count = 0;
        scaleUserProfit(mUserGoldNumTv);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        todayDate = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));

        userInfoPresenterImp = new UserInfoPresenterImp(this, this);
        initInfoPresenterImp = new InitInfoPresenterImp(this, this);
        takeGoldInfoPresenterImp = new TakeGoldInfoPresenterImp(this, this);
        homeDataInfoPresenterImp = new HomeDataInfoPresenterImp(this, this);
        gameTimeInfoPresenterImp = new GameTimeInfoPresenterImp(this, this);
        reportInfoPresenterImp = new ReportInfoPresenterImp(this, this);
        newUserMoneyPresenterImp = new NewUserMoneyPresenterImp(this, this);
        logInfoPresenterImp = new LogInfoPresenterImp(this, this);

        SPUtils.getInstance().put("one_gold_get_date", 0L);
        SPUtils.getInstance().put("two_gold_get_date", 0L);
        SPUtils.getInstance().put("four_gold_get_date", 0L);

        //fourGoldShow();
        //Logger.i("device_id--->" + DeviceUtils.getUniqueDeviceId());
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
        if (type.equals("loaded")) {
            Message message = Message.obtain();
            message.what = 1;
            mHandler.sendMessage(message);
            return;
        } else if (type.equals("upLevel") && !StringUtils.isEmpty(num)) {
            isDouble = 0;
            gamePass(3, "4", oneCurrentGoldNum + "", isDouble);
        } else if (type.equals("currentRecord") && !StringUtils.isEmpty(num)) {
            int tempNum = Integer.parseInt(num);
            Logger.i("num--->" + tempNum);

            if (isFirstStartCount) {
                isFirstStartCount = false;
                Message message = Message.obtain();
                message.what = 2;
                mHandler.sendMessage(message);
            }
        }
    }

    //第1个红包的倒计时
    void oneGoldShow() {
        long diffSecond = 0;
        if (SPUtils.getInstance().getLong("one_gold_get_date", 0L) > 0) {
            long tempDate = SPUtils.getInstance().getLong("one_gold_get_date");
            diffSecond = TimeUtils.getTimeSpanByNow(tempDate + ONE_COUNT_SPACE * 1000, TimeConstants.SEC);
        } else {
            diffSecond = 0;
            long dateNow = TimeUtils.getNowMills();
            SPUtils.getInstance().put("one_gold_get_date", dateNow);
        }

        long dateNow = TimeUtils.getNowMills();
        SPUtils.getInstance().put("one_gold_get_date", dateNow);

        Logger.i("one间隔的时间--->" + diffSecond);
        Logger.i("one FLOAT_SHOW_NUM--->" + FLOAT_SHOW_NUM);
        if (diffSecond == 0 && FLOAT_SHOW_NUM > 0) {
            oneGoldLayout.setVisibility(View.GONE);
            long tempDate = SPUtils.getInstance().getLong("one_gold_get_date");
            diffSecond = TimeUtils.getTimeSpanByNow(tempDate + ONE_COUNT_SPACE * 1000, TimeConstants.SEC);
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
                    oneGoldLayout.setVisibility(View.VISIBLE);
                }
            }.start();
        } else {
            oneGoldLayout.setVisibility(View.GONE);
        }
    }

    //第2个红包的倒计时
    void twoGoldShow() {
        long diffSecond = 0;
        if (SPUtils.getInstance().getLong("two_gold_get_date", 0L) > 0) {
            long tempDate = SPUtils.getInstance().getLong("two_gold_get_date");
            diffSecond = TimeUtils.getTimeSpanByNow(tempDate + TWO_COUNT_SPACE * 1000, TimeConstants.SEC);
        } else {
            diffSecond = 0;
            long dateNow = TimeUtils.getNowMills();
            SPUtils.getInstance().put("two_gold_get_date", dateNow);
        }

        long dateNow = TimeUtils.getNowMills();
        SPUtils.getInstance().put("two_gold_get_date", dateNow);

        Logger.i("two间隔的时间--->" + diffSecond);
        Logger.i("two FLOAT_SHOW_NUM--->" + FLOAT_SHOW_NUM);
        if (diffSecond == 0 && FLOAT_SHOW_NUM > 0) {
            twoGoldLayout.setVisibility(View.GONE);
            long tempDate = SPUtils.getInstance().getLong("two_gold_get_date");
            diffSecond = TimeUtils.getTimeSpanByNow(tempDate + TWO_COUNT_SPACE * 1000, TimeConstants.SEC);
            //翻倍看视频
            twoGoldTimer = new CountDownTimer(diffSecond * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Logger.i("two剩余时间--->" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    long dateNow = TimeUtils.getNowMills();
                    SPUtils.getInstance().put("two_gold_get_date", dateNow);
                    twoGoldLayout.setVisibility(View.VISIBLE);
                }
            }.start();
        } else {
            twoGoldLayout.setVisibility(View.GONE);
        }
    }


    //第4个红包的倒计时
    void fourGoldShow() {
        long diffSecond = 0;
        if (SPUtils.getInstance().getLong("four_gold_get_date", 0L) > 0) {
            long tempDate = SPUtils.getInstance().getLong("four_gold_get_date");
            diffSecond = TimeUtils.getTimeSpanByNow(tempDate + THREE_COUNT_SPACE * 1000, TimeConstants.SEC);
        } else {
            diffSecond = 0;
            long dateNow = TimeUtils.getNowMills();
            SPUtils.getInstance().put("four_gold_get_date", dateNow);
        }

        long dateNow = TimeUtils.getNowMills();
        SPUtils.getInstance().put("four_gold_get_date", dateNow);

        Logger.i("four间隔的时间--->" + diffSecond);

        if (diffSecond == 0) {
            mGameGoldFourTv.setVisibility(View.GONE);
            long tempDate = SPUtils.getInstance().getLong("four_gold_get_date");
            diffSecond = TimeUtils.getTimeSpanByNow(tempDate + THREE_COUNT_SPACE * 1000, TimeConstants.SEC);
            //翻倍看视频
            fourGoldTimer = new CountDownTimer(diffSecond * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Logger.i("four剩余时间--->" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    long dateNow = TimeUtils.getNowMills();
                    SPUtils.getInstance().put("four_gold_get_date", dateNow);
                    mGameGoldFourTv.setVisibility(View.VISIBLE);
                }
            }.start();
        } else {
            mGameGoldFourTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void gamePlayTimeCallback(String gameId, int playTimeInSeconds) {
        Log.d(TAG, "游戏时间--->" + gameId + "," + playTimeInSeconds);
        gameTimeInfoPresenterImp.upGameTime(App.mUserInfo != null ? App.mUserInfo.getId() : "");
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
                gameVideoShow();
                //seeVideo();
            }
        });
    }

    public void gameVideoShow() {
        //ToastUtils.showLong("模拟视频看完，领取金币成功");
        if (mGameVideoAd != null) {
            //step6:在获取到广告后展示
            mGameVideoAd.showRewardVideoAd(this, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "game_video_ad");
            //mttRewardVideoAd = null;
        } else {
            if (loadDialog != null && !loadDialog.isShowing()) {
                loadDialog.show();
            }
            Logger.i("请先加载广告");
            mGameVideoAd = null;
            isReloadVideo = true;
            loadGameVideoAd(restartVideoCodeId, TTAdConstant.VERTICAL);
        }
    }

    public void seeVideo() {
        //ToastUtils.showLong("模拟视频看完，领取金币成功");
        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(this, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "home_video_ad");
            //mttRewardVideoAd = null;
        } else {
            if (loadDialog != null && !loadDialog.isShowing()) {
                loadDialog.show();
            }
            Logger.i("请先加载广告");
            mttRewardVideoAd = null;
            isReloadVideo = true;
            loadVideoAd(windowVideoCodeId, TTAdConstant.VERTICAL);
        }
    }

    @OnClick(R.id.layout_one_gold)
    void playOneVideo() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络未连接，请检查网络后重试");
            return;
        }
        clickIndex = 0;
        ONE_COUNT_SPACE = COUNT_SPACE;
        oneGoldLayout.setVisibility(View.GONE);
        oneCurrentGoldNum = RandomUtils.nextInt(BUBBLE_START, BUBBLE_END);
        isDouble = 0;
        takeGoldNum(3, "1", oneCurrentGoldNum + "", isDouble);
    }

    @OnClick(R.id.layout_two_gold)
    void playTwoVideo() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络未连接，请检查网络后重试");
            return;
        }
        clickIndex = 1;
        TWO_COUNT_SPACE = COUNT_SPACE;
        twoGoldLayout.setVisibility(View.GONE);
        twoCurrentGoldNum = RandomUtils.nextInt(BUBBLE_START, BUBBLE_END);
        isDouble = 1;
        //takeGoldNum(3, "1", twoCurrentGoldNum + "", isDouble);
        clickDoubleGold();
    }

    @OnClick(R.id.tv_game_gold_four)
    void playFourVideo() {
        clickIndex = 3;
        mGameGoldFourTv.setVisibility(View.GONE);
        fourCurrentGoldNum = RandomUtils.nextInt(BUBBLE_START, BUBBLE_END);
        takeGoldNum(3, "1", fourCurrentGoldNum + "", 0);
    }

    /**
     * 游戏通关
     *
     * @param type
     */
    public void gamePass(int type, String taskId, String gold, int state) {
        try {
            clickIndex = 3;
            dialogType = 2;
            TakeGoldInfo takeGoldInfo = new TakeGoldInfo();
            takeGoldInfo.setTaskId(taskId);
            takeGoldInfo.setUserId(App.mUserInfo != null ? App.mUserInfo.getId() : "");
            takeGoldInfo.setGold(Integer.parseInt(gold));
            takeGoldInfo.setIsPlay(state);
            takeGoldInfoPresenterImp.takeStepGold(takeGoldInfo);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 领取金币
     *
     * @param type
     */
    public void takeGoldNum(int type, String taskId, String gold, int state) {

        try {
            dialogType = 1;
            TakeGoldInfo takeGoldInfo = new TakeGoldInfo();
            takeGoldInfo.setTaskId(taskId);
            takeGoldInfo.setUserId(App.mUserInfo != null ? App.mUserInfo.getId() : "");
            takeGoldInfo.setGold(Integer.parseInt(gold));

            if (type == 3) {
                takeGoldInfo.setIsDouble(state);
                if (clickIndex == 1) {
                    takeGoldInfo.setIsDirect(1);
                }
                takeGoldInfoPresenterImp.takeLuckGold(takeGoldInfo);
            }

            if (type == 4) {
                takeGoldInfoPresenterImp.takeTaskGold(takeGoldInfo);
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
        float expressViewHeight = 200;

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
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
                logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "gold_settle_pop", "show");
            }
        });

        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                Logger.i("点击开始下载");
                logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "gold_settle_pop", "click");
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
                logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "gold_settle_pop", "down");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                Logger.i("点击安装");
                logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "gold_settle_pop", "install");
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
                if (loadDialog != null && loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }
                isReloadVideo = false;
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

                //如果是重新加载的广告，则直接播放
                if (isReloadVideo) {
                    if (loadDialog != null && loadDialog.isShowing()) {
                        loadDialog.dismiss();
                    }
                    isReloadVideo = false;
                    mttRewardVideoAd.showRewardVideoAd(GameActivity.this, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "home_video_ad");
                }

                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Logger.i("rewardVideoAd show");
                        logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "gold_double_video", "show");
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
                        loadVideoAd(windowVideoCodeId, TTAdConstant.VERTICAL);
                    }

                    @Override
                    public void onVideoError() {
                        Logger.i("rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        Logger.i("verify:" + rewardVerify + " amount:" + rewardAmount + " name:" + rewardName);
                        SPUtils.getInstance().put("is_show_cash_point", true);

                        if (App.mUserInfo != null && App.mUserInfo.getNewUserTx() == 0) {
                            mCashPointIv.setVisibility(View.VISIBLE);
                        }

                        if (clickIndex > 3) {
                            if (clickIndex == 4) {
                                isDouble = 1;
                                takeGoldNum(4, App.mUserInfo.getNewUserTaskConfig().getId(), "1", isDouble);
                            }
                            if (clickIndex == 5) {
                                isDouble = 1;
                                takeGoldNum(4, App.mUserInfo.getLoginTaskConfig().getId(), "1", isDouble);
                            }
                        } else {
                            int tempResultGold = 0;
                            if (clickIndex == 0) {
                                SPUtils.getInstance().put("one_gold_get_date", 0L);
                                tempResultGold = oneCurrentGoldNum;
                            }
                            if (clickIndex == 1) {
                                SPUtils.getInstance().put("two_gold_get_date", 0L);
                                tempResultGold = twoCurrentGoldNum;
                            }
                            if (clickIndex == 3) {
                                SPUtils.getInstance().put("four_gold_get_date", 0L);
                                tempResultGold = fourCurrentGoldNum;
                            }

                            isDouble = 1;
                            if (dialogType == 1) {
                                takeGoldNum(3, "1", tempResultGold * 2 + "", isDouble);
                            } else {
                                gamePass(3, "4", oneCurrentGoldNum + "", isDouble);
                            }
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
                        logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "gold_double_video", "click");
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
                        logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "gold_double_video", "down");
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        Logger.i("安装完成，点击下载区域打开");
                        logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "gold_double_video", "install");
                    }
                });
            }
        });
    }

    private void loadGameVideoAd(String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setAdCount(1)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("游戏复活") //奖励的名称
                .setRewardAmount(1)  //奖励的数量
                .setUserID("user12345")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("game_rewardVideoAd err code" + code + "---" + message);
                if (loadDialog != null && loadDialog.isShowing()) {
                    loadDialog.dismiss();
                }
                isReloadVideo = false;
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                Logger.i("game_rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                Logger.i("game_rewardVideoAd loaded");
                mGameVideoAd = ad;

                //如果是重新加载的广告，则直接播放
                if (isReloadVideo) {
                    if (loadDialog != null && loadDialog.isShowing()) {
                        loadDialog.dismiss();
                    }
                    isReloadVideo = false;
                    mGameVideoAd.showRewardVideoAd(GameActivity.this, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "home_video_ad");
                }

                mGameVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Logger.i("game_rewardVideoAd show");
                        logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "game_revive_video", "show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Logger.i("game_rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Logger.i("game_rewardVideoAd close");
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        Logger.i("game_rewardVideoAd complete");
                        //缓存下一个视频
                        loadGameVideoAd(restartVideoCodeId, TTAdConstant.VERTICAL);
                    }

                    @Override
                    public void onVideoError() {
                        Logger.i("game_rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        Logger.i("verify:" + rewardVerify + " amount:" + rewardAmount + " name:" + rewardName);
                        SPUtils.getInstance().put("is_show_cash_point", true);
                        if (ycGameAdCallback != null) {
                            ycGameAdCallback.gameAdSuccess();
                            ycGameAdCallback = null;
                        }
                    }

                    @Override
                    public void onSkippedVideo() {
                        Logger.i("game_rewardVideoAd has onSkippedVideo");
                    }
                });
                mGameVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                        logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "game_revive_video", "click");
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
                        logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "game_revive_video", "down");
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        Logger.i("安装完成，点击下载区域打开");
                        logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "game_revive_video", "install");
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
                    //已经领取过新人红包后，
                    if (App.mUserInfo != null && App.mUserInfo.getNewUserTaskConfig().getNewUserGold() == 1) {
                        mUserGoldNumTv.setCharacterLists(TickerUtils.provideNumberList());

                        String totalGold = App.mUserInfo.getGold() + "";
                        if (App.mUserInfo != null) {
                            if (App.mUserInfo.getGold() > 100000) {
                                double total = (double) App.mUserInfo.getGold();
                                double last = total / 10000d;
                                totalGold = (int) last + "";
                                mGoldUnitTv.setVisibility(View.VISIBLE);
                            } else {
                                mGoldUnitTv.setVisibility(View.GONE);
                            }
                        }
                        mUserGoldNumTv.setText(totalGold);
                        mUserAmountNumTv.setText(App.mUserInfo != null ? MatrixUtils.getPrecisionMoney(App.mUserInfo.getAmount()) + "" : "");
                    }

                    if (isFirstLoad) {
                        isFirstLoad = false;
                        initInfoPresenterImp.initInfo(((UserInfoRet) tData).getData().getId());
                        //加载完成，判断是否显示红包
                        showHB();

                        //用户获取串号注册成功后，上报一次数据
                        org.json.JSONObject jsonObject = new org.json.JSONObject();
                        try {
                            jsonObject.put("app_register", 2);
                        } catch (org.json.JSONException e) {
                            e.printStackTrace();
                        }
                        AppLogNewUtils.onEventV3("app_imei_register", jsonObject);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (spaIsShow) {
                                    spaIsShow = false;
                                    logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "game_start_screen", "show");
                                }
                                if (spaIsClick) {
                                    spaIsClick = false;
                                    logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "game_start_screen", "click");
                                }
                                if (spaIsDowned) {
                                    spaIsDowned = false;
                                    logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "game_start_screen", "down");
                                }
                                if (spaIsInstall) {
                                    spaIsInstall = false;
                                    logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "game_start_screen", "install");
                                }
                            }
                        }).start();
                    }
                }
            }

            if (tData instanceof InitInfoRet && ((InitInfoRet) tData).getCode() == Constants.SUCCESS) {
                Logger.i("init info" + JSON.toJSONString(tData));
                if (((InitInfoRet) tData).getData() != null) {
                    App.initInfo = ((InitInfoRet) tData).getData();
                    //最大的可领取的悬浮红包的次数
                    FLOAT_SHOW_NUM = App.initInfo.getUserStepData().getLuckRestNum();
                    App.userTodayStep = App.initInfo.getUserStepData().getStepNum();
                    Logger.i("user today step--->" + App.userTodayStep);

                    //home_bottom_banner(首页横幅)，game_start_screen(游戏开屏)，gold_settle_pop(金币结算)，game_revive_video(游戏复活)，gold_double_video(金币翻倍)，
                    if (((InitInfoRet) tData).getData().getAdList() != null && ((InitInfoRet) tData).getData().getAdList().size() > 0) {
                        List<InitInfo.AdInfo> adList = ((InitInfoRet) tData).getData().getAdList();
                        Logger.i("ad list--->" + JSON.toJSONString(adList));

                        for (int i = 0; i < adList.size(); i++) {
                            if (adList.get(i).getPosition().equals("game_start_screen")) {
                                SPUtils.getInstance().put("spa_code_id", adList.get(i).getCode());
                            }
                            if (adList.get(i).getPosition().equals("home_bottom_banner")) {
                                SPUtils.getInstance().put("home_banner_code_id", adList.get(i).getCode());
                            }
                            if (adList.get(i).getPosition().equals("gold_settle_pop")) {
                                SPUtils.getInstance().put("window_code_id", adList.get(i).getCode());
                            }
                            if (adList.get(i).getPosition().equals("gold_double_video")) {
                                SPUtils.getInstance().put("window_video_code_id", adList.get(i).getCode());
                            }
                            if (adList.get(i).getPosition().equals("game_revive_video")) {
                                SPUtils.getInstance().put("restart_video_code_id", adList.get(i).getCode());
                            }
                        }
                    }
                }

                //初始化首页数据
                homeDataInfoPresenterImp.initHomeData();
            }

            if (tData instanceof HomeDateInfoRet && ((HomeDateInfoRet) tData).getData() != null) {
                homeDataInfo = ((HomeDateInfoRet) tData).getData();
                //气泡的间隔时间
                COUNT_SPACE = homeDataInfo.getBubbleTaskInfo().getInterval();
            }

            if (tData instanceof TakeGoldInfoRet) {
                Logger.i("take gold info--->" + JSON.toJSONString(tData));
                if (((TakeGoldInfoRet) tData).getCode() == Constants.SUCCESS) {
                    if (((TakeGoldInfoRet) tData).getData() != null) {
                        TakeGoldInfo takeGoldInfo = ((TakeGoldInfoRet) tData).getData();
                        //获取剩下的可领取的次数
                        FLOAT_SHOW_NUM = takeGoldInfo.getLuckRestNum();

                        if (App.mUserInfo != null) {
                            App.mUserInfo.setGold(takeGoldInfo.getGold());
                        }

                        mUserAmountNumTv.setText(MatrixUtils.getPrecisionMoney(takeGoldInfo.getAmount()) + "");

                        //打开翻倍
                        if (isDouble == 0) {
                            if (receiveDoubleGoldDialog == null) {
                                receiveDoubleGoldDialog = new ReceiveDoubleGoldDialog(GameActivity.this, R.style.gold_dialog);
                                receiveDoubleGoldDialog.setGoldDoubleDialogListener(this);
                            }
                            receiveDoubleGoldDialog.show();
                            receiveDoubleGoldDialog.updateCommonInfo(takeGoldInfo.getTakeGold() + "", takeGoldInfo.getGold() + "", "≈" + MatrixUtils.getPrecisionMoney(takeGoldInfo.getAmount()) + "元", adView, dialogType);
                        } else {
                            //不翻倍弹窗
                            if (receiveGoldDialog == null) {
                                receiveGoldDialog = new ReceiveGoldDialog(GameActivity.this, R.style.gold_dialog);
                                receiveGoldDialog.setGoldDialogListener(this);
                            }
                            receiveGoldDialog.show();
                            receiveGoldDialog.updateCommonInfo(takeGoldInfo.getTakeGold() + "", takeGoldInfo.getGold() + "", "≈" + MatrixUtils.getPrecisionMoney(takeGoldInfo.getAmount()) + "元", adView, dialogType);
                        }

                        //加载一次弹窗的广告
                        loadExpressAd(windowCodeId);
                        //游戏底部banner
                        loadBannerExpressAd(homeBannerCodeId);

                        //结算金币上报数据
                        int upGold = takeGoldInfo.getTakeGold() / 100;

                        if (upGold < 10) {
                            upGold = 10;
                        }
                        if (upGold > 100) {
                            upGold = 100;
                        }
                        EventUtils.setPurchase(null, null, null, 1, null, null, true, upGold);

                    } else {
                        ToastUtils.showLong(((TakeGoldInfoRet) tData).getMsg());
                    }
                } else {

                    ToastUtils.showLong(((TakeGoldInfoRet) tData).getMsg());
                }
            }

            if (tData instanceof GameTimeInfoRet && ((GameTimeInfoRet) tData).getCode() == Constants.SUCCESS) {
                Logger.i("up game time--->" + JSON.toJSONString(tData));
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        userInfoPresenterImp.imeiLogin(PhoneUtils.getIMEI(), App.agentId, App.softId, App.appName);
    }

    @OnClick(R.id.iv_task_icon)
    void taskList() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络未连接，请检查网络后重试");
            return;
        }
        Intent intent = new Intent(this, TaskActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick({R.id.layout_user_money, R.id.iv_user_cash})
    void goToHongBao() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络未连接，请检查网络后重试");
            return;
        }
        Intent intent = new Intent(this, CashActivity.class);
        startActivityForResult(intent, 1);
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
    public void closeGoldDialog() {
        hideBottomMenu();
        if (receiveGoldDialog != null && receiveGoldDialog.isShowing()) {
            receiveGoldDialog.dismiss();
            receiveGoldDialog = null;
        }

        if (clickIndex == 0 && dialogType == 1) {
            SPUtils.getInstance().put("one_gold_get_date", 0L);
            if (oneGoldTimer != null) {
                oneGoldTimer.cancel();
                oneGoldTimer = null;
            }
            oneGoldShow();
            ONE_COUNT_SPACE = COUNT_SPACE;
        }

        if (clickIndex == 1 && dialogType == 1) {
            SPUtils.getInstance().put("two_gold_get_date", 0L);
            if (twoGoldTimer != null) {
                twoGoldTimer.cancel();
                twoGoldTimer = null;
            }

            twoGoldShow();
            TWO_COUNT_SPACE = COUNT_SPACE;
        }

        //关闭广告弹窗时，移除adView
        if (adView != null && adView.getParent() != null) {
            ((ViewGroup) adView.getParent()).removeView(adView);
        }
        startGoldTask();
    }

    @Override
    public void closeDoubleGoldDialog() {
        hideBottomMenu();
        doubleDialogFunction();
        //展现金币动效
        startGoldTask();
    }

    public void doubleDialogFunction() {
        if (receiveDoubleGoldDialog != null && receiveDoubleGoldDialog.isShowing()) {
            receiveDoubleGoldDialog.dismiss();
            receiveDoubleGoldDialog = null;
        }

        if (clickIndex == 0 && dialogType == 1) {
            SPUtils.getInstance().put("one_gold_get_date", 0L);
            if (oneGoldTimer != null) {
                oneGoldTimer.cancel();
                oneGoldTimer = null;
            }
            oneGoldShow();
            ONE_COUNT_SPACE = COUNT_SPACE;
        }

        if (clickIndex == 1 && dialogType == 1) {
            SPUtils.getInstance().put("two_gold_get_date", 0L);
            if (twoGoldTimer != null) {
                twoGoldTimer.cancel();
                twoGoldTimer = null;
            }
            twoGoldShow();
            TWO_COUNT_SPACE = COUNT_SPACE;
        }

        //关闭广告弹窗时，移除adView
        if (adView != null && adView.getParent() != null) {
            ((ViewGroup) adView.getParent()).removeView(adView);
        }

    }

    @Override
    public void clickDoubleGold() {
        doubleDialogFunction();

        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(this, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "home_video_ad");
            //mttRewardVideoAd = null;
        } else {
            if (loadDialog != null && !loadDialog.isShowing()) {
                loadDialog.show();
            }
            Logger.i("请先加载广告");
            mttRewardVideoAd = null;
            isReloadVideo = true;
            loadVideoAd(windowVideoCodeId, TTAdConstant.VERTICAL);
        }
    }

    @OnClick(R.id.layout_gold_detail)
    public void goldDetail() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showLong("网络未连接，请检查网络后重试");
            return;
        }

        Intent intent = new Intent(this, MyWalletActivity.class);
        startActivity(intent);
    }

    @Override
    public void openHongBao(int type) {
        if (clickIndex == 4) {
            if (hongBaoDialog != null && !hongBaoDialog.isShowing()) {
                //hongBaoDialog.autoOpenHongBao(App.initInfo != null ? App.initInfo.getNewTaskConfig().getGold() : 0);
            }
        }
        if (clickIndex == 5) {
            seeVideo();
        }
    }

    @Override
    public void startMakeMoney() {
        hideBottomMenu();
        /*mUserGoldNumTv.setCharacterLists(TickerUtils.provideNumberList());

        String totalGold = App.mUserInfo.getGold() + "";
        if (App.mUserInfo != null) {
            if (App.mUserInfo.getGold() > 100000) {
                double total = (double) App.mUserInfo.getGold();
                double last = total / 10000d;
                totalGold = (int) last + "";
                mGoldUnitTv.setVisibility(View.VISIBLE);
            } else {
                mGoldUnitTv.setVisibility(View.GONE);
            }
        }
        mUserGoldNumTv.setText(totalGold);*/

        //展现金币动效
        startGoldTask();
        mUserAmountNumTv.setText(App.mUserInfo != null ? MatrixUtils.getPrecisionMoney(App.mUserInfo.getAmount()) + "" : "");
        newUserMoneyPresenterImp.newUserMoney(App.mUserInfo != null ? App.mUserInfo.getId() : "");
    }

    @Override
    public void closeHongBao() {
        hideBottomMenu();
        mTaskPointIv.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_game_rule)
    void gameRule() {
        if (gameRuleDialog != null && !gameRuleDialog.isShowing()) {
            gameRuleDialog.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mForceGoMain = true;
    }

    @BindView(R.id.splash_container)
    FrameLayout mSplashContainer;
    //是否强制跳转到主页面
    private boolean mForceGoMain;
    //开屏广告加载发生超时但是SDK没有及时回调结果的时候，做的一层保护。
    private final WeakHandler adHandler = new WeakHandler(this);
    //开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
    private static final int AD_TIME_OUT = 5000;
    private static final int MSG_GO_MAIN = 1;
    //开屏广告是否已经加载
    private boolean mHasLoaded;

    /**
     * 加载开屏广告
     */
    private void loadSplashAd() {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(spaCodeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                Log.d(TAG, message);
                mHasLoaded = true;
                Logger.i("open ad error--->" + message);
                splashAdFinish();
            }

            @Override
            @MainThread
            public void onTimeout() {
                mHasLoaded = true;
                Logger.i("开屏广告加载超时");
                splashAdFinish();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                Log.d(TAG, "开屏广告请求成功");
                mHasLoaded = true;
                mHandler.removeCallbacksAndMessages(null);
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                if (view != null) {
                    mSplashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    mSplashContainer.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();
                } else {
                    splashAdFinish();
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d(TAG, "onAdClicked");
                        Logger.i("开屏广告点击");
                        spaIsClick = true;
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d(TAG, "onAdShow");
                        Logger.i("开屏广告展示");
                        //logInfoPresenterImp.addLogInfo(App);
                        spaIsShow = true;
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d(TAG, "onAdSkip");
                        Logger.i("开屏广告跳过");
                        splashAdFinish();
                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d(TAG, "onAdTimeOver");
                        Logger.i("开屏广告倒计时结束");
                        splashAdFinish();
                    }
                });
                if (ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                Logger.i("下载中...");
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            Logger.i("下载暂停...");

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            Logger.i("下载失败...");

                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                            //ToastUtils.showLong("开屏广告下载完成");
                            Logger.i("spa down finish--->");
                            spaIsDowned = true;
                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {
                            //ToastUtils.showLong("开屏广告安装完成--->");
                            Logger.i("spa install finish--->");
                            spaIsInstall = true;
                        }
                    });
                }
            }
        }, AD_TIME_OUT);

    }

    private void loadBannerExpressAd(String codeId) {
        mBannerExpressContainer.removeAllViews();
        float expressViewWidth = SizeUtils.px2dp(ScreenUtils.getScreenWidth());
        float expressViewHeight = 50;

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(3) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("load error : " + code + ", " + message);
                mBannerExpressContainer.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mBannerTTAd = ads.get(0);
                mBannerTTAd.setSlideIntervalTime(30 * 1000);
                bindBannerAdListener(mBannerTTAd);
                startBannerTime = System.currentTimeMillis();
                if (mBannerTTAd != null) {
                    mBannerTTAd.render();
                }
            }
        });
    }

    private long startBannerTime = 0;

    private boolean mBannerHasShowDownloadActive = false;

    private void bindBannerAdListener(TTNativeExpressAd ad) {
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
                Log.e("ExpressView", "render fail:" + (System.currentTimeMillis() - startBannerTime));
                Logger.i(msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startBannerTime));
                //返回view的宽高 单位 dp
                Logger.i("渲染成功");
                mBannerExpressContainer.removeAllViews();
                mBannerExpressContainer.addView(view);
                logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "home_bottom_banner", "show");
            }
        });

        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                Logger.i("点击开始下载");
                logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "home_bottom_banner", "click");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mBannerHasShowDownloadActive) {
                    mBannerHasShowDownloadActive = true;
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
                logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "home_bottom_banner", "down");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                Logger.i("点击安装");
                logInfoPresenterImp.addLogInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", "", "", "home_bottom_banner", "install");
            }
        });
    }


    @Override
    public void handleMsg(Message msg) {
        if (msg.what == MSG_GO_MAIN) {
            if (!mHasLoaded) {
                //showToast("广告已超时，跳到主页面");
                splashAdFinish();
                Logger.i("广告超时");
            }
        }
    }

    /**
     * 开屏广告结束
     */
    public void splashAdFinish() {
        mSplashContainer.removeAllViews();
        mSplashContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaHelper.pause();
        MediaHelper.release();
        NetworkUtils.unregisterNetworkStatusChangedListener(this);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void gameRuleClose() {
        hideBottomMenu();
    }

    @Override
    public void onDisconnected() {
        Logger.i("game net work onDisconnected--->");
    }

    @Override
    public void onConnected(NetworkUtils.NetworkType networkType) {
        Logger.i("game net work onConnected--->");
        //网络重新连接时，重新请求网络加载数据
        readPhoneTask();
    }
}
