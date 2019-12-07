package com.ydys.moneywalk.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PathUtils;
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
import com.jaeger.library.StatusBarUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.HomeDataInfo;
import com.ydys.moneywalk.bean.HomeDateInfoRet;
import com.ydys.moneywalk.bean.MessageEvent;
import com.ydys.moneywalk.bean.TakeGoldInfo;
import com.ydys.moneywalk.bean.TakeGoldInfoRet;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.bean.UserStepInfo;
import com.ydys.moneywalk.bean.UserStepInfoRet;
import com.ydys.moneywalk.bean.VersionInfo;
import com.ydys.moneywalk.bean.VersionInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.HomeDataInfoPresenterImp;
import com.ydys.moneywalk.presenter.TakeGoldInfoPresenterImp;
import com.ydys.moneywalk.presenter.UserInfoPresenterImp;
import com.ydys.moneywalk.presenter.UserStepInfoPresenterImp;
import com.ydys.moneywalk.presenter.VersionInfoPresenterImp;
import com.ydys.moneywalk.ui.activity.InviteFriendActivity;
import com.ydys.moneywalk.ui.activity.MakeMoneyActivity;
import com.ydys.moneywalk.ui.activity.PhoneLoginActivity;
import com.ydys.moneywalk.ui.custom.DislikeDialog;
import com.ydys.moneywalk.ui.custom.ExceedDialog;
import com.ydys.moneywalk.ui.custom.GlideImageLoader;
import com.ydys.moneywalk.ui.custom.LoginDialog;
import com.ydys.moneywalk.ui.custom.ReceiveDoubleGoldDialog;
import com.ydys.moneywalk.ui.custom.ReceiveGoldDialog;
import com.ydys.moneywalk.ui.custom.StepNumProgressView;
import com.ydys.moneywalk.ui.custom.VersionDialog;
import com.ydys.moneywalk.ui.custom.step.BindService;
import com.ydys.moneywalk.ui.custom.step.UpdateUiCallBack;
import com.ydys.moneywalk.util.MatrixUtils;
import com.ydys.moneywalk.util.RandomUtils;
import com.ydys.moneywalk.util.TTAdManagerHolder;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class HomeFragment extends BaseFragment implements IBaseView, SwipeRefreshLayout.OnRefreshListener, LoginDialog.LoginListener, ReceiveGoldDialog.GoldDialogListener, ReceiveDoubleGoldDialog.GoldDoubleDialogListener, ExceedDialog.ExceedListener, VersionDialog.VersionListener {

    public static int EXCHANGE_SCALE = 10;

    public static int BUBBLE_START = 10;

    public static int BUBBLE_END = 35;

    public static int COUNT_SPACE = 1 * 60;

    public static double STEP_METRE = 0.6;

    //步行的运动系数
    public static double CLL_SCALE = 0.8214;

    //默认的成年人体重(kg)
    public static int WEIGHT = 70;

    //每天基础步数的起始随机值
    public static int TODAY_MIN = 30;

    public static int TODAY_MAX = 100;

    //低版本手机的随机值-最小
    public static int LOW_MIN = 1000;
    //低版本手机的随机值-最大
    public static int LOW_MAX = 2000;

    //单次领取金币的上线，超过后需要观看视频
    public static int GET_ON_LINE = 200;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.temp_view)
    StepNumProgressView mStepNumProgress;

    @BindView(R.id.tv_gold_one)
    TextView mOneGoldTv;

    @BindView(R.id.tv_gold_two)
    TextView mTwoGoldTv;

    @BindView(R.id.tv_gold_three)
    TextView mThreeGoldTv;

    @BindView(R.id.layout_gold_four)
    LinearLayout mFourGoldLayout;

    @BindView(R.id.tv_step_gold_num)
    TextView mStepGoldNumTv;

    @BindView(R.id.tv_kilo_metre)
    TextView mKiloMetreTv;

    @BindView(R.id.tv_hour)
    TextView mHourTv;

    @BindView(R.id.tv_minute)
    TextView mMinuteTv;

    @BindView(R.id.tv_calorie)
    TextView mCalorieTv;

    @BindView(R.id.banner)
    Banner mBanner;

    @BindView(R.id.btn_get_gold)
    Button mGetGoldBtn;

    @BindView(R.id.tv_share_gold)
    TextView mShareGoldTv;

    private int timeCount = 0;

    private BindService bindService;

    private boolean isBind;

    private String todayDate;

    //当前实时的总步数
    private int currentStepNum;

    //“步数金币”已经兑换过的步数
    private int isExchangeStepNum;

    private CountDownTimer oneGoldTimer;

    private CountDownTimer twoGoldTimer;

    private CountDownTimer threeGoldTimer;

    private int receiveGoldTitleIndex = 0;

    //当前步数对应的刻度进度
    private int currentProgress;

    private HomeDataInfoPresenterImp homeDataInfoPresenterImp;

    private UserStepInfoPresenterImp userStepInfoPresenterImp;

    private TakeGoldInfoPresenterImp takeGoldInfoPresenterImp;

    private VersionInfoPresenterImp versionInfoPresenterImp;

    private HomeDataInfo homeDataInfo;

    private List<String> receiveTitles;

    //随机产生当日的起始步数
    private int todayStartStep;

    ReceiveGoldDialog receiveGoldDialog;

    LoginDialog loginDialog;

    private String currentTakeGold;

    private UMShareAPI mShareAPI = null;

    private ProgressDialog progressDialog = null;

    UserInfoPresenterImp userInfoPresenterImp;

    ReceiveDoubleGoldDialog receiveDoubleGoldDialog;

    ExceedDialog exceedDialog;

    VersionDialog versionDialog;

    private VersionInfo versionInfo;

    BaseDownloadTask task;

    private int goldType = 1;

    //幸运金币可以领取的次数
    private int goldCanGetNum = 100;

    //广告配置
    private TTAdNative mTTAdNative;

    private TTAdDislike mTTAdDislike;

    private TTNativeExpressAd mTTAd;

    private TTRewardVideoAd mttRewardVideoAd;

    private View adView;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    setStepAndProgress();
                    break;
                case 2:
                    int progress = (Integer) msg.obj;
                    versionDialog.updateProgress(progress);
                    break;
                case 3:
                    versionDialog.downFinish();
                    break;
                case 4:
                    mShareGoldTv.setText(App.initInfo != null ? App.initInfo.getInviteConfig().getGold() + "" : "");
                    //处理3个金币的显示
                    oneStart();
                    twoStart();
                    threeStart();
                    break;
                case 5:
                    mOneGoldTv.setVisibility(goldCanGetNum > 0 ? View.VISIBLE : View.GONE);
                    mOneGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
                    break;
                case 6:
                    mTwoGoldTv.setVisibility(goldCanGetNum > 0 ? View.VISIBLE : View.GONE);
                    mTwoGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
                    break;
                case 7:
                    mThreeGoldTv.setVisibility(goldCanGetNum > 0 ? View.VISIBLE : View.GONE);
                    mThreeGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
                    break;
                case 8:
                    mOneGoldTv.setVisibility(goldCanGetNum > 0 ? View.VISIBLE : View.GONE);
                    break;
                case 9:
                    mTwoGoldTv.setVisibility(goldCanGetNum > 0 ? View.VISIBLE : View.GONE);
                    break;
                case 10:
                    mThreeGoldTv.setVisibility(goldCanGetNum > 0 ? View.VISIBLE : View.GONE);
                    break;
                    default:
                        break;
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    public void initVars() {
        EventBus.getDefault().register(this);
        setTopViewBgColor();
    }

    public void setTopViewBgColor() {
        Logger.i("top view color home--->");
        StatusBarUtil.setLightMode(getActivity());
        StatusBarUtil.setColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.transparent), 0);
    }

    @Override
    public void initViews() {
        FileDownloader.setup(getActivity());

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(getActivity());

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在登录");

        mShareAPI = UMShareAPI.get(getActivity());

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(true, -0, 200);
        swipeRefreshLayout.setProgressViewEndTarget(true, 180);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary), Color.RED, Color.YELLOW, Color.BLUE);

        loginDialog = new LoginDialog(getActivity(), R.style.common_dialog);
        loginDialog.setLoginListener(this);

        receiveGoldDialog = new ReceiveGoldDialog(getActivity(), R.style.gold_dialog);
        receiveGoldDialog.setGoldDialogListener(this);

        receiveDoubleGoldDialog = new ReceiveDoubleGoldDialog(getActivity(), R.style.gold_dialog);
        receiveDoubleGoldDialog.setGoldDoubleDialogListener(this);

        exceedDialog = new ExceedDialog(getActivity(), R.style.common_dialog);
        exceedDialog.setExceedListener(this);

        versionDialog = new VersionDialog(getActivity(), R.style.common_dialog);
        versionDialog.setVersionListener(this);
        versionDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (versionInfo != null && versionInfo.getForceUpdate() == 1) {
                        return true;//不执行父类点击事件
                    }
                    return false;
                }
                return false;
            }
        });
    }

    @Override
    public void loadData() {
        homeDataInfoPresenterImp = new HomeDataInfoPresenterImp(this, getActivity());
        userStepInfoPresenterImp = new UserStepInfoPresenterImp(this, getActivity());
        takeGoldInfoPresenterImp = new TakeGoldInfoPresenterImp(this, getActivity());
        userInfoPresenterImp = new UserInfoPresenterImp(this, getActivity());
        versionInfoPresenterImp = new VersionInfoPresenterImp(this, getActivity());

        todayDate = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));

        receiveTitles = new ArrayList<>();

        //setStepAndProgress();

        //随机产生气泡金币
        //randomBubbleGoldNum();

        List<Animator> animators = new ArrayList<>();
        ObjectAnimator translationYAnim1 = ObjectAnimator.ofFloat(mOneGoldTv, "translationY", -12.0f, 12.0f, -12.0f);
        translationYAnim1.setDuration(2500);
        translationYAnim1.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim1.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim1);

        ObjectAnimator translationYAnim2 = ObjectAnimator.ofFloat(mTwoGoldTv, "translationY", -10.0f, 10.0f, -10.0f);
        translationYAnim2.setDuration(2200);
        translationYAnim2.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim2.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim2);

        ObjectAnimator translationYAnim3 = ObjectAnimator.ofFloat(mThreeGoldTv, "translationY", -12.0f, 12.0f, -12.0f);
        translationYAnim3.setDuration(2000);
        translationYAnim3.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim3.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim3);

        ObjectAnimator translationYAnim4 = ObjectAnimator.ofFloat(mFourGoldLayout, "translationY", -11.0f, 11.0f, -11.0f);
        translationYAnim4.setDuration(2500);
        translationYAnim4.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim4.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim4);

        AnimatorSet btnSexAnimatorSet = new AnimatorSet();
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.setStartDelay(100);
        btnSexAnimatorSet.start();


        List<Integer> bannerList = new ArrayList<>();
        bannerList.add(R.mipmap.home_banner);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(bannerList);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (position == 0) {
                    if (App.mUserInfo == null || !SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false)) {
                        if (loginDialog != null && !loginDialog.isShowing()) {
                            loginDialog.show();
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
                        intent.putExtra("share_type", 1);
                        startActivity(intent);
                    }
                }
            }
        });

        //PermissionUtils.launchAppDetailsSettings();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("init_success")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loadExpressAd(Constants.BANNER_CODE_ID);
                    loadVideoAd(Constants.VIDEO_CODE_ID, TTAdConstant.VERTICAL);

                    //可领取次数
                    goldCanGetNum = App.initInfo != null ? App.initInfo.getUserStepData().getLuckRestNum() : 100;

                    if (App.initInfo != null && App.initInfo.getUserStepData().getWeight() > 0) {
                        WEIGHT = App.initInfo.getUserStepData().getWeight();
                    }

                    Message message = Message.obtain();
                    message.what = 4;
                    mHandler.sendMessage(message);

                    //初始化首页数据
                    homeDataInfoPresenterImp.initHomeData();
                    versionInfoPresenterImp.updateVersion(App.agentId);
                }
            }).start();
        }

        if (messageEvent.getMessage().equals("update_weight")) {
            if (App.initInfo != null && App.initInfo.getUserStepData().getWeight() > 0) {
                WEIGHT = App.initInfo.getUserStepData().getWeight();
            }

            //设置公里数及步行时间
            double kilometre = (currentStepNum * STEP_METRE) / 1000;
            //计算卡路里
            double tempCll = CLL_SCALE * WEIGHT * kilometre;
            mCalorieTv.setText(MatrixUtils.getPrecisionMoney(tempCll));
        }
    }

    //低版本手机，打开的间隔时间超过2小时，随机生成一个
    public void randomStepInLowDevice() {
        if (SPUtils.getInstance().getLong(todayDate + "_last_open_date", 0L) == 0) {
            SPUtils.getInstance().put(todayDate + "_last_open_date", TimeUtils.getNowMills());
        } else {
            long lastDate = SPUtils.getInstance().getLong(todayDate + "_last_open_date");
            long tempHour = TimeUtils.getTimeSpanByNow(lastDate, TimeConstants.HOUR);
            Logger.i("temp space--->" + tempHour);
            if (-tempHour > 1) {
                int tempStep = RandomUtils.nextInt(LOW_MIN, LOW_MAX);
                SPUtils.getInstance().put(todayDate, tempStep);
            }
            SPUtils.getInstance().put(todayDate + "_last_open_date", TimeUtils.getNowMills());
        }
    }

    /**
     * 启动计步服务
     */
    public void startStepService() {
        Intent intent = new Intent(getActivity(), BindService.class);
        isBind = getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        getActivity().startService(intent);
    }

    //当前所走的步数所处的阶段
    public int stepInStageNum() {
        int index = 0;
        if (homeDataInfo != null && homeDataInfo.getStageTaskInfo().getList() != null && homeDataInfo.getStageTaskInfo().getList().size() > 0) {
            List<HomeDataInfo.StageInfo> tempList = homeDataInfo.getStageTaskInfo().getList();
            for (int i = 0; i < tempList.size(); i++) {
                if (currentStepNum >= tempList.get(i).getStepNum()) {
                    index = i;
                }
            }
        }
        return index;
    }


    public void setStepAndProgress() {
        App.newStepNum = currentStepNum;

        SPUtils.getInstance().put(todayDate + "_" + Constants.NEW_STEP_NUM, currentStepNum);

        //设置实时的步数及刻度进度值
        mStepNumProgress.setWalkStepNum(currentStepNum);
        currentProgress = currentStepNum / 100;
        if (currentProgress > 60) {
            currentProgress = 60;
        }
        Logger.i("progress--->" + currentProgress);
        mStepNumProgress.setStepProgress(currentProgress);

        //设置标题值
//        if (currentStepNum >= 0 && currentStepNum < 1500) {
//            receiveGoldTitleIndex = 0;
//        } else if (currentStepNum > 1500 && currentStepNum < 3000) {
//            receiveGoldTitleIndex = 1;
//        } else if (currentStepNum > 3000 && currentStepNum < 4500) {
//            receiveGoldTitleIndex = 2;
//        } else if (currentStepNum > 4500 && currentStepNum < 6000) {
//            receiveGoldTitleIndex = 3;
//        } else {
//            receiveGoldTitleIndex = 4;
//        }

        if (App.initInfo != null && App.initInfo.getStageTaskFinish() == 0) {
            receiveGoldTitleIndex = stepInStageNum();
            if (receiveGoldTitleIndex < SPUtils.getInstance().getInt(Constants.IS_GET_STAGE + "_" + todayDate)) {
                receiveGoldTitleIndex = SPUtils.getInstance().getInt(Constants.IS_GET_STAGE + "_" + todayDate);
                mGetGoldBtn.setText("继续努力");
                mGetGoldBtn.setBackgroundResource(R.mipmap.continue_bg);
            } else {
                mGetGoldBtn.setText("领取金币");
                mGetGoldBtn.setBackgroundResource(R.mipmap.get_gold_btn_bg);
            }
        } else {
            mGetGoldBtn.setText("目标完成");
            mGetGoldBtn.setBackgroundResource(R.mipmap.continue_bg);
        }
        if (App.mUserInfo != null && App.mUserInfo.getIsBind() == 1 && !SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false)) {
            mGetGoldBtn.setText("去登录");
            mGetGoldBtn.setBackgroundResource(R.mipmap.get_gold_btn_bg);
        }

        mStepNumProgress.updateStateTitle(receiveTitles, receiveGoldTitleIndex);

        //设置步数金币的显示/隐藏，数值
        int getGoldNum = (currentStepNum - isExchangeStepNum) / 10;
        //总步数 < 20000，且未领取的的步数 > 10
        if (currentStepNum < 20000 && getGoldNum > 0) {
            mFourGoldLayout.setVisibility(View.VISIBLE);
            mStepGoldNumTv.setText(getGoldNum + "");
        } else {
            mFourGoldLayout.setVisibility(View.GONE);
        }

        //设置公里数及步行时间
        double kilometre = (currentStepNum * STEP_METRE) / 1000;
        mKiloMetreTv.setText(MatrixUtils.getPrecisionMoney(kilometre));
        //设置步行时间: 100步 == 1分钟
        int minutes = currentStepNum / 100;
        if (minutes < 1) {
            minutes = 1;
        }
        int tempHour = minutes / 60 > 24 ? 24 : minutes / 60;
        int tempMinute = minutes % 60 > 60 ? 60 : minutes % 60;
        mHourTv.setText(String.format("%02d", tempHour));
        mMinuteTv.setText(String.format("%02d", tempMinute));
        //计算卡路里
        double tempCll = CLL_SCALE * WEIGHT * kilometre;
        mCalorieTv.setText(MatrixUtils.getPrecisionMoney(tempCll));
    }

    //随机产生气泡
    public void randomBubbleGoldNum() {
        Logger.i("start --->" + BUBBLE_START + "---end--->" + BUBBLE_END);

        mOneGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
        mTwoGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
        mThreeGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
    }

    //进度到顶后，开始回退
    public void numberBack() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (timeCount > currentProgress) {
                    timeCount = timeCount - 2;
                    Logger.i("timeCount--->" + timeCount);
                    mStepNumProgress.setStepProgress(timeCount);
                    mHandler.postDelayed(this, 1);
                } else {
                    mStepNumProgress.setStepProgress(currentProgress);
                }
            }
        }, 1);
    }

    @OnClick(R.id.layout_get_gold)
    void makeGoldMethod() {
        Intent intent = new Intent(getActivity(), MakeMoneyActivity.class);
        startActivity(intent);
    }

    /**
     * 设备绑定过，但是本地未登录
     *
     * @return
     */
    public boolean bindButNotLogin() {
        return (App.mUserInfo != null && App.mUserInfo.getIsBind() == 1 && !SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false));
    }

    /**
     * 领取金币
     */
    @OnClick(R.id.btn_get_gold)
    void getStateGold() {

        //此设备绑定过账户，但是本地未登录
        if (App.mUserInfo == null || bindButNotLogin()) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        if (App.initInfo != null && App.initInfo.getStageTaskFinish() == 1) {
            return;
        }

        int tempIndex = stepInStageNum();

        //设置标题值
//        if (currentStepNum >= 0 && currentStepNum < 1500) {
//            tempIndex = 0;
//        } else if (currentStepNum > 1500 && currentStepNum < 3000) {
//            tempIndex = 1;
//        } else if (currentStepNum > 3000 && currentStepNum < 4500) {
//            tempIndex = 2;
//        } else if (currentStepNum > 4500 && currentStepNum < 6000) {
//            tempIndex = 3;
//        } else {
//            tempIndex = 4;
//        }

        if (SPUtils.getInstance().getInt(Constants.IS_GET_STAGE + "_" + todayDate) <= tempIndex) {
            int takeGold = homeDataInfo != null ? homeDataInfo.getStageTaskInfo().getList().get(tempIndex).getGold() : 0;
            takeGoldNum(3, takeGold + "", tempIndex + 1);

            //ToastUtils.showLong("领取成功");
            tempIndex++;
            if (tempIndex > 4) {
                tempIndex = 4;
            }
            mStepNumProgress.updateStateTitle(receiveTitles, tempIndex);
            SPUtils.getInstance().put(Constants.IS_GET_STAGE + "_" + todayDate, tempIndex);

            mGetGoldBtn.setText("继续努力");
            mGetGoldBtn.setBackgroundResource(R.mipmap.continue_bg);
        } else {
            //ToastUtils.showLong("已经领取过，请继续步行");
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        //timeCount = currentProgress;
        timeCount = 0;

        //刷新时数值增加
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                if (timeCount < 60) {
                    timeCount++;
                    Logger.i("timeCount--->" + timeCount);
                    mStepNumProgress.setStepProgress(timeCount);
                    mHandler.postDelayed(this, 1);
                } else {
                    numberBack();
                }
            }
        }, 1000);
    }
    //int todayRandomStepNum = 0;

    //和绑定服务数据交换的桥梁，可以通过IBinder service获取服务的实例来调用服务的方法或者数据
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            //当天有步数记录
            if (App.userTodayStep > 0 || SPUtils.getInstance().getInt(todayDate + "_" + Constants.NEW_STEP_NUM) > 0) {
                todayStartStep = SPUtils.getInstance().getInt(todayDate + "_random_step", 0);
                if (todayStartStep > 0) {
                    int tempWalkNum = SPUtils.getInstance().getInt(todayDate, 0);
                    if (tempWalkNum == 0) {
                        currentStepNum = todayStartStep + SPUtils.getInstance().getInt(todayDate + "_" + Constants.NEW_STEP_NUM);
                    } else {
                        currentStepNum = todayStartStep + tempWalkNum;
                    }
                    //同步步数
                    sendStepToServer(currentStepNum);
                } else {
                    todayStartStep = App.userTodayStep;
                    currentStepNum = todayStartStep;
                }
            } else {
                todayStartStep = RandomUtils.nextInt(TODAY_MIN, TODAY_MAX);
                Logger.i("产生随机步数--->" + todayStartStep);
                //当天的初始化步数
                SPUtils.getInstance().put(todayDate + "_random_step", todayStartStep);
                currentStepNum = todayStartStep;
                //同步步数
                sendStepToServer(todayStartStep);
            }

            //低版本手机的处理
            if (App.isLowDevice) {
                randomStepInLowDevice();
            }

            //currentStepNum = currentStepNum + SPUtils.getInstance().getInt(todayDate, 0);
            Logger.i("onServiceConnected--->" + currentStepNum);

            isExchangeStepNum = SPUtils.getInstance().getInt(todayDate + Constants.CURRENT_DAY_EXCHANGE_STEP, 0);
            setStepAndProgress();

            BindService.LcBinder lcBinder = (BindService.LcBinder) service;
            bindService = lcBinder.getService();
            bindService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    Logger.i("current step --->" + stepCount);
                    currentStepNum = todayStartStep + stepCount;
                    isExchangeStepNum = SPUtils.getInstance().getInt(todayDate + Constants.CURRENT_DAY_EXCHANGE_STEP, 0);
                    //当前接收到stepCount数据，就是最新的步数
                    Message message = Message.obtain();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
     * 同步步数到服务器
     */
    public void sendStepToServer(int step) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UserStepInfo userStepInfo = new UserStepInfo();
                userStepInfo.setUserId(App.mUserInfo != null ? App.mUserInfo.getId() : "");
                userStepInfo.setStepNum(step);
                userStepInfoPresenterImp.updateStepInfo(userStepInfo);
            }
        }, 1000);
    }

    //步数金币领取
    @OnClick(R.id.layout_gold_four)
    void stepExchange() {
        //此设备绑定过账户，但是本地未登录
        if (App.mUserInfo == null || bindButNotLogin()) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }

        //步数气泡显示的实时金币数
        int tempGold = Integer.parseInt(mStepGoldNumTv.getText().toString());
        if (tempGold >= GET_ON_LINE) {
            if (exceedDialog != null && !exceedDialog.isShowing()) {
                exceedDialog.show();
            }
        } else {
            isExchangeStepNum = currentStepNum - currentStepNum % EXCHANGE_SCALE;
            mFourGoldLayout.setVisibility(View.GONE);
            SPUtils.getInstance().put(todayDate + Constants.CURRENT_DAY_EXCHANGE_STEP, isExchangeStepNum);
            takeGoldNum(2, mStepGoldNumTv.getText().toString(), 0);
        }
    }

    void oneStart() {
        if (SPUtils.getInstance().getBoolean("one_over", false)) {
            mOneGoldTv.setVisibility(goldCanGetNum > 0 ? View.VISIBLE : View.GONE);
        } else {
            oneBubbleGoldNum();
        }
    }

    void twoStart() {
        if (SPUtils.getInstance().getBoolean("two_over", false)) {
            mTwoGoldTv.setVisibility(goldCanGetNum > 0 ? View.VISIBLE : View.GONE);
        } else {
            twoBubbleGoldNum();
        }
    }

    void threeStart() {
        if (SPUtils.getInstance().getBoolean("three_over", false)) {
            mThreeGoldTv.setVisibility(goldCanGetNum > 0 ? View.VISIBLE : View.GONE);
        } else {
            threeBubbleGoldNum();
        }
    }

    /**
     * 领取金币
     *
     * @param type
     */
    public void takeGoldNum(int type, String gold, int state) {
        goldType = type;
        currentTakeGold = gold;

        try {
            TakeGoldInfo takeGoldInfo = new TakeGoldInfo();
            takeGoldInfo.setTaskId(homeDataInfo != null ? homeDataInfo.getBubbleTaskInfo().getId() : "");
            takeGoldInfo.setUserId(App.mUserInfo != null ? App.mUserInfo.getId() : "");
            takeGoldInfo.setGold(Integer.parseInt(gold));
            if (type == 1 || type == 4) {
                takeGoldInfo.setIsDouble(state);
                takeGoldInfoPresenterImp.takeLuckGold(takeGoldInfo);
            }

            if (type == 2) {
                takeGoldInfo.setIsPlay(state);
                takeGoldInfoPresenterImp.takeStepGold(takeGoldInfo);
            }
            if (type == 3) {
                takeGoldInfo.setStage(state);
                takeGoldInfoPresenterImp.takeStageGold(takeGoldInfo);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_gold_one)
    void oneClick() {
        //此设备绑定过账户，但是本地未登录
        if (App.mUserInfo == null || bindButNotLogin()) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }
        oneBubbleGoldNum();
    }

    @OnClick(R.id.tv_gold_two)
    void twoClick() {
        //此设备绑定过账户，但是本地未登录
        if (App.mUserInfo == null || bindButNotLogin()) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }
        twoBubbleGoldNum();
    }

    @OnClick(R.id.tv_gold_three)
    void threeClick() {
        //此设备绑定过账户，但是本地未登录
        if (App.mUserInfo == null || bindButNotLogin()) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
            return;
        }
        threeBubbleGoldNum();
    }

    void oneBubbleGoldNum() {
        long diffSecond;
        if (SPUtils.getInstance().getLong("one_gold_get_date", 0L) > 0) {
            if (SPUtils.getInstance().getBoolean("one_over", false)) {
                long dateNow = TimeUtils.getNowMills();
                SPUtils.getInstance().put("one_gold_get_date", dateNow);
            }
            long tempDate = SPUtils.getInstance().getLong("one_gold_get_date");
            diffSecond = TimeUtils.getTimeSpanByNow(tempDate + COUNT_SPACE * 1000, TimeConstants.SEC);
        } else {
            diffSecond = 0;
            long dateNow = TimeUtils.getNowMills();
            SPUtils.getInstance().put("one_gold_get_date", dateNow);
        }

        Logger.i("间隔的时间--->" + diffSecond);

        if (diffSecond > 0) {

            takeGoldNum(1, mOneGoldTv.getText().toString(), 0);

            SPUtils.getInstance().put("one_over", false);
            mOneGoldTv.setVisibility(View.GONE);
            //翻倍看视频
            oneGoldTimer = new CountDownTimer(diffSecond * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Logger.i("one剩余时间--->" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    SPUtils.getInstance().put("one_over", true);
                    Message message = Message.obtain();
                    message.what = 5;
                    mHandler.sendMessage(message);
                }
            }.start();
        } else {
            SPUtils.getInstance().put("one_over", true);
            Message message = Message.obtain();
            message.what = 8;
            mHandler.sendMessage(message);
        }
    }

    void twoBubbleGoldNum() {
        long diffSecond;

        if (SPUtils.getInstance().getLong("two_gold_get_date", 0L) > 0) {
            if (SPUtils.getInstance().getBoolean("two_over", false)) {
                long dateNow = TimeUtils.getNowMills();
                SPUtils.getInstance().put("two_gold_get_date", dateNow);
            }
            long tempDate = SPUtils.getInstance().getLong("two_gold_get_date");
            diffSecond = TimeUtils.getTimeSpanByNow(tempDate + COUNT_SPACE * 1000, TimeConstants.SEC);
        } else {
            diffSecond = 0;
            long dateNow = TimeUtils.getNowMills();
            SPUtils.getInstance().put("two_gold_get_date", dateNow);
        }

        Logger.i("间隔的时间--->" + diffSecond);

        if (diffSecond > 0) {
            takeGoldNum(1, mTwoGoldTv.getText().toString(), 0);

            SPUtils.getInstance().put("two_over", false);
            mTwoGoldTv.setVisibility(View.GONE);
            //翻倍看视频
            twoGoldTimer = new CountDownTimer(diffSecond * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Logger.i("two剩余时间--->" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    SPUtils.getInstance().put("two_over", true);
                    Message message = Message.obtain();
                    message.what = 6;
                    mHandler.sendMessage(message);
                }
            }.start();
        } else {
            SPUtils.getInstance().put("two_over", true);
            Message message = Message.obtain();
            message.what = 9;
            mHandler.sendMessage(message);
        }
    }

    void threeBubbleGoldNum() {
        long diffSecond;

        if (SPUtils.getInstance().getLong("three_gold_get_date", 0L) > 0) {
            if (SPUtils.getInstance().getBoolean("three_over", false)) {
                long dateNow = TimeUtils.getNowMills();
                SPUtils.getInstance().put("three_gold_get_date", dateNow);
            }
            long tempDate = SPUtils.getInstance().getLong("three_gold_get_date");
            diffSecond = TimeUtils.getTimeSpanByNow(tempDate + COUNT_SPACE * 1000, TimeConstants.SEC);
        } else {
            diffSecond = 0;
            long dateNow = TimeUtils.getNowMills();
            SPUtils.getInstance().put("three_gold_get_date", dateNow);
        }

        Logger.i("间隔的时间--->" + diffSecond);

        if (diffSecond > 0) {
            takeGoldNum(1, mThreeGoldTv.getText().toString(), 0);

            SPUtils.getInstance().put("three_over", false);
            mThreeGoldTv.setVisibility(View.GONE);
            //翻倍看视频
            threeGoldTimer = new CountDownTimer(diffSecond * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Logger.i("three剩余时间--->" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    SPUtils.getInstance().put("three_over", true);
                    Message message = Message.obtain();
                    message.what = 7;
                    mHandler.sendMessage(message);
                }
            }.start();
        } else {
            SPUtils.getInstance().put("three_over", true);
            Message message = Message.obtain();
            message.what = 10;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void onDestroy() {  //app被关闭之前，service先解除绑定
        super.onDestroy();
        if (isBind) {
            getActivity().unbindService(serviceConnection);
        }
        EventBus.getDefault().unregister(this);
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
            if (tData instanceof HomeDateInfoRet && ((HomeDateInfoRet) tData).getCode() == Constants.SUCCESS) {
                Logger.i("home data--->" + JSON.toJSONString(tData));
                if (((HomeDateInfoRet) tData).getData() != null) {
                    homeDataInfo = ((HomeDateInfoRet) tData).getData();
                    //气泡的间隔时间
                    COUNT_SPACE = homeDataInfo.getBubbleTaskInfo().getInterval();
                    //领取上限
                    GET_ON_LINE = homeDataInfo.getStepTaskInfo().getGoldLimit();
                    BUBBLE_START = homeDataInfo.getBubbleTaskInfo().getGoldMin();
                    BUBBLE_END = homeDataInfo.getBubbleTaskInfo().getGoldMax();

                    TODAY_MIN = homeDataInfo.getOneStepMin();
                    TODAY_MAX = homeDataInfo.getOneStepMax();
                    LOW_MIN = homeDataInfo.getTwoStepMin();
                    LOW_MAX = homeDataInfo.getTwoDtepMax();

                    if (homeDataInfo.getStageTaskInfo() != null && homeDataInfo.getStageTaskInfo().getList() != null && homeDataInfo.getStageTaskInfo().getList().size() > 0) {
                        for (int i = 0; i < homeDataInfo.getStageTaskInfo().getList().size(); i++) {
                            String tempTitle = "满" + homeDataInfo.getStageTaskInfo().getList().get(i).getStepNum() + "步领取" + homeDataInfo.getStageTaskInfo().getList().get(i).getGold() + "金币";
                            receiveTitles.add(tempTitle);
                        }
                        Logger.i("titles --->" + JSON.toJSONString(receiveTitles));
                    }

                    mStepNumProgress.updateStateTitle(receiveTitles, receiveGoldTitleIndex);

                    startStepService();
                }
            }

            if (tData instanceof UserStepInfoRet && ((UserStepInfoRet) tData).getCode() == Constants.SUCCESS) {
                Logger.i("result info--->" + JSON.toJSONString(tData));
            }

            if (tData instanceof TakeGoldInfoRet) {
                Logger.i("take gold info--->" + JSON.toJSONString(tData));
                if (((TakeGoldInfoRet) tData).getCode() == Constants.SUCCESS) {
                    if (((TakeGoldInfoRet) tData).getData() != null) {
                        TakeGoldInfo takeGoldInfo = ((TakeGoldInfoRet) tData).getData();
                        goldCanGetNum = takeGoldInfo.getLuckRestNum();
                        //可翻倍
                        if (goldType == 1) {
                            if (receiveDoubleGoldDialog == null) {
                                receiveDoubleGoldDialog = new ReceiveDoubleGoldDialog(getActivity(), R.style.gold_dialog);
                                receiveDoubleGoldDialog.setGoldDoubleDialogListener(this);
                            }
                            receiveDoubleGoldDialog.show();
                            receiveDoubleGoldDialog.updateGoldInfo(currentTakeGold, takeGoldInfo.getGold() + "", "≈" + MatrixUtils.getPrecisionMoney(takeGoldInfo.getAmount()) + "元", adView);
                        } else if (goldType == 2) {
                            if (receiveDoubleGoldDialog == null) {
                                receiveDoubleGoldDialog = new ReceiveDoubleGoldDialog(getActivity(), R.style.gold_dialog);
                                receiveDoubleGoldDialog.setGoldDoubleDialogListener(this);
                            }
                            receiveDoubleGoldDialog.show();
                            int exchangeStep = Integer.parseInt(mStepGoldNumTv.getText().toString()) * homeDataInfo.getStepTaskInfo().getStepNum();
                            receiveDoubleGoldDialog.updateGoldByStep(currentTakeGold, takeGoldInfo.getGold() + "", "≈" + MatrixUtils.getPrecisionMoney(takeGoldInfo.getAmount()) + "元", exchangeStep, adView);
                        } else {
                            //不可翻倍
                            if (receiveGoldDialog == null) {
                                receiveGoldDialog = new ReceiveGoldDialog(getActivity(), R.style.gold_dialog);
                                receiveGoldDialog.setGoldDialogListener(this);
                            }
                            receiveGoldDialog.show();
                            receiveGoldDialog.updateGoldInfo(currentTakeGold, takeGoldInfo.getGold() + "", "≈" + MatrixUtils.getPrecisionMoney(takeGoldInfo.getAmount()) + "元", adView);

                            if (receiveGoldTitleIndex == 4) {
                                mGetGoldBtn.setText("目标完成");
                                mGetGoldBtn.setBackgroundResource(R.mipmap.continue_bg);
                                App.initInfo.setStageTaskFinish(1);
                            }
                        }

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

            if (tData instanceof UserInfoRet) {
                if (((UserInfoRet) tData).getCode() == Constants.SUCCESS) {
                    Toasty.normal(getActivity(), "登录成功").show();
                    //存储用户信息
                    SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(((UserInfoRet) tData).getData()));
                    SPUtils.getInstance().put(Constants.LOCAL_LOGIN, true);
                    App.mUserInfo = ((UserInfoRet) tData).getData();
                    App.isLogin = true;

                    if (receiveGoldTitleIndex < SPUtils.getInstance().getInt(Constants.IS_GET_STAGE + "_" + todayDate)) {
                        receiveGoldTitleIndex = SPUtils.getInstance().getInt(Constants.IS_GET_STAGE + "_" + todayDate);
                        mGetGoldBtn.setText("继续努力");
                        mGetGoldBtn.setBackgroundResource(R.mipmap.continue_bg);
                    } else {
                        mGetGoldBtn.setText("领取金币");
                        mGetGoldBtn.setBackgroundResource(R.mipmap.get_gold_btn_bg);
                    }
                } else {
                    Toasty.normal(getActivity(), ((UserInfoRet) tData).getMsg()).show();
                }
            }

            if (tData instanceof VersionInfoRet) {
                if (((VersionInfoRet) tData).getCode() == Constants.SUCCESS) {
                    if (((VersionInfoRet) tData).getData() != null) {
                        versionInfo = ((VersionInfoRet) tData).getData();

                        int currentCode = AppUtils.getAppVersionCode();
                        if (versionInfo != null && versionInfo.getVersionCode() > currentCode) {
                            if (versionDialog != null && !versionDialog.isShowing()) {
                                versionDialog.setVersionName(versionInfo.getVersionNum());
                                versionDialog.setVersionContent(versionInfo.getUpdateContent());
                                versionDialog.setIsForceUpdate(versionInfo.getForceUpdate());
                                versionDialog.show();
                            }
                        } else {
                            //Toasty.normal(this, "已经是最新版本").show();
                            Logger.i("已经是最新版本--->" + currentCode);
                        }
                    }
                }
            }
        }

        randomBubbleGoldNum();
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        Logger.e("error--->" + throwable.getMessage());
        randomBubbleGoldNum();
    }

    @OnClick(R.id.layout_invite)
    void inviteFriend() {
        if (App.mUserInfo == null || !SPUtils.getInstance().getBoolean(Constants.LOCAL_LOGIN, false)) {
            if (loginDialog != null && !loginDialog.isShowing()) {
                loginDialog.show();
            }
        } else {
            Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
            intent.putExtra("share_type", 1);
            startActivity(intent);
        }
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
            Toasty.normal(getActivity(), "授权失败").show();
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
            Toasty.normal(getActivity(), "授权取消").show();
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
        mShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, authListener);
    }

    @Override
    public void phoneLogin() {
        Intent intent = new Intent(getActivity(), PhoneLoginActivity.class);
        startActivity(intent);
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
    public void seeVideo() {
        //ToastUtils.showLong("模拟视频看完，领取金币成功");
        if (mttRewardVideoAd != null) {
            goldType = 2;
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(getActivity(), TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "home_video_ad");
            //mttRewardVideoAd = null;
        } else {
            Logger.i("请先加载广告");
        }
    }

    @Override
    public void cancelSeeVideo() {

    }

    @Override
    public void versionUpdate() {
        if (versionInfo != null && !StringUtils.isEmpty(versionInfo.getDownUrl())) {
            downAppFile(versionInfo.getDownUrl());
        }
    }

    @Override
    public void cancelUpdate() {

    }

    public void downAppFile(String downUrl) {
        Logger.i("down url --->" + downUrl);

        final String filePath = PathUtils.getExternalAppFilesPath() + "/new_walk.apk";
        Logger.i("down app path --->" + filePath);

        task = FileDownloader.getImpl().create(downUrl)
                .setPath(filePath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        //Toasty.normal(SettingActivity.this, "正在更新版本后...").show();
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int progress = (int) ((soFarBytes * 1.0 / totalBytes) * 100);
                        Logger.i("progress--->" + soFarBytes + "---" + totalBytes + "---" + progress);

                        Message message = new Message();
                        message.what = 2;
                        message.obj = progress;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Toasty.normal(getActivity(), "下载完成").show();
                        Message message = new Message();
                        message.what = 3;
                        mHandler.sendMessage(message);

                        AppUtils.installApp(filePath);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });

        task.start();
    }

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

    private long startTime = 0;

    private boolean mHasShowDownloadActive = false;

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

            final DislikeDialog dislikeDialog = new DislikeDialog(getActivity(), words);
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
        ad.setDislikeCallback(getActivity(), new TTAdDislike.DislikeInteractionCallback() {
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
                        if (goldType == 4) {
                            //视频看完，请求翻倍奖励
                            takeGoldNum(goldType, currentTakeGold, 1);
                        }

                        if (goldType == 2) {
                            isExchangeStepNum = currentStepNum - currentStepNum % EXCHANGE_SCALE;
                            mFourGoldLayout.setVisibility(View.GONE);
                            SPUtils.getInstance().put(todayDate + Constants.CURRENT_DAY_EXCHANGE_STEP, isExchangeStepNum);
                            takeGoldNum(goldType, mStepGoldNumTv.getText().toString(), 0);
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
    public void clickDoubleGold() {

        closeDoubleGoldDialog();

        if (mttRewardVideoAd != null) {
            goldType = 4;
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(getActivity(), TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "home_video_ad");
            //mttRewardVideoAd = null;
        } else {
            Logger.i("请先加载广告");
        }
    }
}
