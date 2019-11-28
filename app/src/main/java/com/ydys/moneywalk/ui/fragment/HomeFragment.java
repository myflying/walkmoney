package com.ydys.moneywalk.ui.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.HomeDataInfo;
import com.ydys.moneywalk.bean.HomeDateInfoRet;
import com.ydys.moneywalk.bean.MessageEvent;
import com.ydys.moneywalk.bean.ResultInfo;
import com.ydys.moneywalk.bean.TakeGoldInfo;
import com.ydys.moneywalk.bean.TakeGoldInfoRet;
import com.ydys.moneywalk.bean.UserStepInfo;
import com.ydys.moneywalk.bean.UserStepInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.HomeDataInfoPresenterImp;
import com.ydys.moneywalk.presenter.TakeGoldInfoPresenterImp;
import com.ydys.moneywalk.presenter.UserStepInfoPresenterImp;
import com.ydys.moneywalk.ui.activity.MakeMoneyActivity;
import com.ydys.moneywalk.ui.custom.Constant;
import com.ydys.moneywalk.ui.custom.GlideImageLoader;
import com.ydys.moneywalk.ui.custom.ReceiveGoldDialog;
import com.ydys.moneywalk.ui.custom.StepNumProgressView;
import com.ydys.moneywalk.ui.custom.step.BindService;
import com.ydys.moneywalk.ui.custom.step.UpdateUiCallBack;
import com.ydys.moneywalk.util.RandomUtils;
import com.ydys.moneywalk.view.HomeDataInfoView;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.PublicKey;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IBaseView {

    public static int EXCHANGE_SCALE = 10;

    public static int BUBBLE_START = 10;

    public static int BUBBLE_END = 35;

    public static int COUNT_SPACE = 1 * 60;

    public static double STEP_METRE = 0.6;

    //不行的运动系数
    public static double CLL_SCALE = 0.8214;

    //默认的成年人体重(kg)
    public static int WEIGHT = 70;

    //每天基础步数的起始随机值
    public static int TODAY_MIN = 30;

    public static int TODAY_MAX = 100;

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

    DecimalFormat df = new DecimalFormat("0.00");

    //当前步数对应的刻度进度
    private int currentProgress;

    private HomeDataInfoPresenterImp homeDataInfoPresenterImp;

    private UserStepInfoPresenterImp userStepInfoPresenterImp;

    private TakeGoldInfoPresenterImp takeGoldInfoPresenterImp;

    private HomeDataInfo homeDataInfo;

    private List<String> receiveTitles;

    //随机产生当日的起始步数
    private int todayStartStep;

    ReceiveGoldDialog receiveGoldDialog;

    private String currentTakeGold;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                setStepAndProgress();
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
        StatusBarUtil.setLightMode(getActivity());
        StatusBarUtil.setColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.white), 0);
    }

    @Override
    public void initViews() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(true, -0, 200);
        swipeRefreshLayout.setProgressViewEndTarget(true, 180);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary), Color.RED, Color.YELLOW, Color.BLUE);

        receiveGoldDialog = new ReceiveGoldDialog(getActivity(), R.style.common_dialog);
    }

    @Override
    public void loadData() {
        homeDataInfoPresenterImp = new HomeDataInfoPresenterImp(this, getActivity());
        userStepInfoPresenterImp = new UserStepInfoPresenterImp(this, getActivity());
        takeGoldInfoPresenterImp = new TakeGoldInfoPresenterImp(this, getActivity());

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
        bannerList.add(R.mipmap.a);
        bannerList.add(R.mipmap.b);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(bannerList);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();

        //处理3个金币的显示
        oneStart();
        twoStart();
        threeStart();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("init_success")) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //初始化首页数据
                    homeDataInfoPresenterImp.initHomeData();
                }
            }).start();
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

    public void setStepAndProgress() {
        //设置实时的步数及刻度进度值
        mStepNumProgress.setWalkStepNum(currentStepNum);
        currentProgress = currentStepNum / 100;
        if (currentProgress > 60) {
            currentProgress = 60;
        }
        Logger.i("progress--->" + currentProgress);
        mStepNumProgress.setStepProgress(currentProgress);

        //设置标题值
        if (currentStepNum >= 0 && currentStepNum < 1500) {
            receiveGoldTitleIndex = 0;
        } else if (currentStepNum > 1500 && currentStepNum < 3000) {
            receiveGoldTitleIndex = 1;
        } else if (currentStepNum > 3000 && currentStepNum < 4500) {
            receiveGoldTitleIndex = 2;
        } else if (currentStepNum > 4500 && currentStepNum < 6000) {
            receiveGoldTitleIndex = 3;
        } else {
            receiveGoldTitleIndex = 4;
        }

        if (receiveGoldTitleIndex < SPUtils.getInstance().getInt(Constants.IS_GET_STAGE)) {
            receiveGoldTitleIndex = SPUtils.getInstance().getInt(Constants.IS_GET_STAGE);
            mGetGoldBtn.setText("继续努力");
            mGetGoldBtn.setBackgroundResource(R.mipmap.continue_bg);
        } else {
            mGetGoldBtn.setText("领取金币");
            mGetGoldBtn.setBackgroundResource(R.mipmap.get_gold_btn_bg);
        }

        mStepNumProgress.updateStateTitle(receiveTitles, receiveGoldTitleIndex);

        //设置步数金币的显示/隐藏，数值
        int getGoldNum = (currentStepNum - isExchangeStepNum) / 10;
        if (getGoldNum > 0) {
            mFourGoldLayout.setVisibility(View.VISIBLE);
            mStepGoldNumTv.setText(getGoldNum + "");
        } else {
            mFourGoldLayout.setVisibility(View.GONE);
        }

        //设置公里数及步行时间
        double kilometre = (currentStepNum * STEP_METRE) / 1000;
        mKiloMetreTv.setText(df.format(kilometre));
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
        mCalorieTv.setText(df.format(tempCll));
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
     * 领取金币
     */
    @OnClick(R.id.btn_get_gold)
    void getStateGold() {

        if (currentStepNum < 10) {
            return;
        }

        int tempIndex;
        //设置标题值
        if (currentStepNum >= 0 && currentStepNum < 1500) {
            tempIndex = 0;
        } else if (currentStepNum > 1500 && currentStepNum < 3000) {
            tempIndex = 1;
        } else if (currentStepNum > 3000 && currentStepNum < 4500) {
            tempIndex = 2;
        } else if (currentStepNum > 4500 && currentStepNum < 6000) {
            tempIndex = 3;
        } else {
            tempIndex = 4;
        }

        if (SPUtils.getInstance().getInt(Constants.IS_GET_STAGE) <= tempIndex) {

            int takeGold = homeDataInfo != null ? homeDataInfo.getStageTaskInfo().getList().get(tempIndex).getGold() : 0;
            takeGoldNum(3, takeGold + "", tempIndex + 1);

            ToastUtils.showLong("领取成功");
            tempIndex++;
            if (tempIndex > 4) {
                tempIndex = 4;
            }
            mStepNumProgress.updateStateTitle(receiveTitles, tempIndex);
            SPUtils.getInstance().put(Constants.IS_GET_STAGE, tempIndex);

            mGetGoldBtn.setText("继续努力");
            mGetGoldBtn.setBackgroundResource(R.mipmap.continue_bg);
        } else {
            ToastUtils.showLong("已经领取过，请继续步行");
        }

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        timeCount = currentProgress;

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

    //和绑定服务数据交换的桥梁，可以通过IBinder service获取服务的实例来调用服务的方法或者数据
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            todayStartStep = App.userTodayStep == 0 ? SPUtils.getInstance().getInt(todayDate + "_random_step", 0) : App.userTodayStep;

            if (todayStartStep == 0) {
                todayStartStep = RandomUtils.nextInt(TODAY_MIN, TODAY_MAX);
                Logger.i("产生随机步数--->" + todayStartStep);
                currentStepNum = todayStartStep;
                SPUtils.getInstance().put(todayDate + "_random_step", todayStartStep);

                //同步步数
                sendStepToServer();
            } else {
                currentStepNum = todayStartStep;
            }
            currentStepNum = currentStepNum + SPUtils.getInstance().getInt(todayDate, 0);

            Logger.i("onServiceConnected--->" + currentStepNum);

            isExchangeStepNum = SPUtils.getInstance().getInt(todayDate + Constants.CURRENT_DAY_EXCHANGE_STEP, 0);
            setStepAndProgress();

            BindService.LcBinder lcBinder = (BindService.LcBinder) service;
            bindService = lcBinder.getService();
            bindService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    currentStepNum = todayStartStep + stepCount;
                    isExchangeStepNum = SPUtils.getInstance().getInt(todayDate + Constants.CURRENT_DAY_EXCHANGE_STEP, 0);
                    //当前接收到stepCount数据，就是最新的步数
                    Message message = Message.obtain();
                    message.what = 1;
                    mHandler.sendMessage(message);
                    Logger.i("current step --->" + stepCount);
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
    void sendStepToServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserStepInfo userStepInfo = new UserStepInfo();
                userStepInfo.setUserId(App.mUserInfo != null ? App.mUserInfo.getId() : "");
                userStepInfo.setStepNum(todayStartStep);

                userStepInfoPresenterImp.updateStepInfo(userStepInfo);
            }
        }).start();
    }

    //步数金币领取
    @OnClick(R.id.layout_gold_four)
    void stepExchange() {
        isExchangeStepNum = currentStepNum - currentStepNum % EXCHANGE_SCALE;
        mFourGoldLayout.setVisibility(View.GONE);

        SPUtils.getInstance().put(todayDate + Constants.CURRENT_DAY_EXCHANGE_STEP, isExchangeStepNum);

        takeGoldNum(2, mStepGoldNumTv.getText().toString(), 0);
    }

    void oneStart() {
        if (SPUtils.getInstance().getBoolean("one_over", false)) {
            mOneGoldTv.setVisibility(View.VISIBLE);
        } else {
            oneBubbleGoldNum();
        }
    }

    void twoStart() {
        if (SPUtils.getInstance().getBoolean("two_over", false)) {
            mTwoGoldTv.setVisibility(View.VISIBLE);
        } else {
            twoBubbleGoldNum();
        }
    }

    void threeStart() {
        if (SPUtils.getInstance().getBoolean("three_over", false)) {
            mThreeGoldTv.setVisibility(View.VISIBLE);
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
        currentTakeGold = gold;

        TakeGoldInfo takeGoldInfo = new TakeGoldInfo();
        takeGoldInfo.setTaskId(homeDataInfo != null ? homeDataInfo.getBubbleTaskInfo().getId() : "");
        takeGoldInfo.setUserId(App.mUserInfo != null ? App.mUserInfo.getId() : "");
        takeGoldInfo.setGold(gold);
        if (type == 1) {
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
    }

    @OnClick(R.id.tv_gold_one)
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
                    mOneGoldTv.setVisibility(View.VISIBLE);
                    mOneGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
                }
            }.start();
        } else {
            SPUtils.getInstance().put("one_over", true);
            mOneGoldTv.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_gold_two)
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
                    mTwoGoldTv.setVisibility(View.VISIBLE);
                    mTwoGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
                }
            }.start();
        } else {
            SPUtils.getInstance().put("two_over", true);
            mTwoGoldTv.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_gold_three)
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
                    mThreeGoldTv.setVisibility(View.VISIBLE);
                    mThreeGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
                }
            }.start();
        } else {
            SPUtils.getInstance().put("three_over", true);
            mThreeGoldTv.setVisibility(View.VISIBLE);
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
        if (tData != null) {

            if (tData instanceof HomeDateInfoRet && ((HomeDateInfoRet) tData).getCode() == Constants.SUCCESS) {
                Logger.i("home data--->" + JSON.toJSONString(tData));
                if (((HomeDateInfoRet) tData).getData() != null) {
                    homeDataInfo = ((HomeDateInfoRet) tData).getData();
                    BUBBLE_START = homeDataInfo.getBubbleTaskInfo().getGoldMin();
                    BUBBLE_END = homeDataInfo.getBubbleTaskInfo().getGoldMax();

                    TODAY_MIN = homeDataInfo.getOneStepMin();
                    TODAY_MAX = homeDataInfo.getOneStepMax();

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

            if (tData instanceof TakeGoldInfoRet && ((TakeGoldInfoRet) tData).getCode() == Constants.SUCCESS) {
                Logger.i("take gold info--->" + JSON.toJSONString(tData));
                if (receiveGoldDialog != null && !receiveGoldDialog.isShowing()) {
                    receiveGoldDialog.show();
                    receiveGoldDialog.updateGoldInfo(currentTakeGold, "1000", "≈1.0元");
                }
            }
        }

        randomBubbleGoldNum();
    }

    @Override
    public void loadDataError(Throwable throwable) {
        Logger.e("error--->" + throwable.getMessage());
        randomBubbleGoldNum();
    }
}
