package com.ydys.moneywalk.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.ui.custom.GlideImageLoader;
import com.ydys.moneywalk.ui.custom.StepNumProgressView;
import com.ydys.moneywalk.ui.custom.step.BindService;
import com.ydys.moneywalk.ui.custom.step.UpdateUiCallBack;
import com.ydys.moneywalk.util.RandomUtils;
import com.youth.banner.Banner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static int EXCHANGE_SCALE = 10;

    public static int BUBBLE_START = 10;

    public static int BUBBLE_END = 40;

    public static int COUNT_SPACE = 2 * 60;

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

    @BindView(R.id.banner)
    Banner mBanner;

    int count = 0;

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
    }

    @Override
    public void loadData() {
        todayDate = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));

        setStepAndProgress();

        //随机产生气泡金币
        randomBubbleGoldNum();

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

        Intent intent = new Intent(getActivity(), BindService.class);
        isBind = getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        getActivity().startService(intent);
    }

    public void setStepAndProgress() {
        mStepNumProgress.setWalkStepNum(currentStepNum);
        int progress = currentStepNum / 100;
        if (progress > 60) {
            progress = 60;
        }
        Logger.i("progress--->" + progress);
        mStepNumProgress.setStepProgress(progress);

        int getGoldNum = (currentStepNum - isExchangeStepNum) / 10;
        if (getGoldNum > 0) {
            mFourGoldLayout.setVisibility(View.VISIBLE);
            mStepGoldNumTv.setText(getGoldNum + "");
        } else {
            mFourGoldLayout.setVisibility(View.GONE);
        }

        if (currentStepNum > 1 && currentStepNum < 1500) {
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
        }

        mStepNumProgress.updateStateTitle(receiveGoldTitleIndex);
    }

    //随机产生气泡
    public void randomBubbleGoldNum() {
        mOneGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
        mTwoGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
        mThreeGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
    }

    //进度到顶后，开始回退
    public void numberBack() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count > 0) {
                    count = count - 2;
                    Logger.i("count--->" + count);
                    mStepNumProgress.setStepProgress(count);
                    mHandler.postDelayed(this, 1);
                } else {
                    //mStepNumProgress.setWalkStepNum(200);
                }
            }
        }, 1);
    }

    /**
     * 领取金币
     */
    @OnClick(R.id.btn_get_gold)
    void getStateGold() {
        receiveGoldTitleIndex++;
        mStepNumProgress.updateStateTitle(receiveGoldTitleIndex);
        SPUtils.getInstance().put(Constants.IS_GET_STAGE, receiveGoldTitleIndex);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        //刷新时数值增加
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                if (count < 60) {
                    count++;
                    Logger.i("count--->" + count);
                    mStepNumProgress.setStepProgress(count);
                    mHandler.postDelayed(this, 1);
                } else {
                    numberBack();
                }
            }
        }, 1000);
    }

    //和绷定服务数据交换的桥梁，可以通过IBinder service获取服务的实例来调用服务的方法或者数据
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            currentStepNum = SPUtils.getInstance().getInt(todayDate, 0);
            isExchangeStepNum = SPUtils.getInstance().getInt(Constants.CURRENT_DAY_EXCHANGE_STEP, 0);
            Logger.i("onServiceConnected--->" + currentStepNum);

            setStepAndProgress();

            BindService.LcBinder lcBinder = (BindService.LcBinder) service;
            bindService = lcBinder.getService();
            bindService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    currentStepNum = stepCount;
                    isExchangeStepNum = SPUtils.getInstance().getInt(Constants.CURRENT_DAY_EXCHANGE_STEP, 0);
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

    //步数金币领取
    @OnClick(R.id.layout_gold_four)
    void stepExchange() {
        isExchangeStepNum = currentStepNum - currentStepNum % EXCHANGE_SCALE;
        mFourGoldLayout.setVisibility(View.GONE);

        SPUtils.getInstance().put(Constants.CURRENT_DAY_EXCHANGE_STEP, isExchangeStepNum);
    }

    @OnClick(R.id.tv_gold_one)
    void oneBubbleGoldNum() {
        mOneGoldTv.setVisibility(View.GONE);
        long dateNow = TimeUtils.getNowMills();
        SPUtils.getInstance().put("one_gold_get_date", dateNow);

        long diffSecond = TimeUtils.getTimeSpanByNow(dateNow + COUNT_SPACE * 1000, TimeConstants.SEC);
        Logger.i("间隔的时间--->" + diffSecond);
        //翻倍看视频
        oneGoldTimer = new CountDownTimer(diffSecond * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Logger.i("one剩余时间--->" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                mOneGoldTv.setVisibility(View.VISIBLE);
                mOneGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
            }
        }.start();
    }

    @OnClick(R.id.tv_gold_two)
    void twoBubbleGoldNum() {
        mTwoGoldTv.setVisibility(View.GONE);
        long dateNow = TimeUtils.getNowMills();
        SPUtils.getInstance().put("two_gold_get_date", dateNow);

        long diffSecond = TimeUtils.getTimeSpanByNow(dateNow + COUNT_SPACE * 1000, TimeConstants.SEC);
        Logger.i("间隔的时间--->" + diffSecond);
        //翻倍看视频
        twoGoldTimer = new CountDownTimer(diffSecond * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Logger.i("two剩余时间--->" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                mTwoGoldTv.setVisibility(View.VISIBLE);
                mTwoGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
            }
        }.start();
    }

    @OnClick(R.id.tv_gold_three)
    void threeBubbleGoldNum() {
        mThreeGoldTv.setVisibility(View.GONE);
        long dateNow = TimeUtils.getNowMills();
        SPUtils.getInstance().put("three_gold_get_date", dateNow);

        long diffSecond = TimeUtils.getTimeSpanByNow(dateNow + COUNT_SPACE * 1000, TimeConstants.SEC);
        Logger.i("间隔的时间--->" + diffSecond);
        //翻倍看视频
        threeGoldTimer = new CountDownTimer(diffSecond * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Logger.i("three剩余时间--->" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                mThreeGoldTv.setVisibility(View.VISIBLE);
                mThreeGoldTv.setText(RandomUtils.nextInt(BUBBLE_START, BUBBLE_END) + "");
            }
        }.start();
    }

    @Override
    public void onDestroy() {  //app被关闭之前，service先解除绑定
        super.onDestroy();
        if (isBind) {
            getActivity().unbindService(serviceConnection);
        }
    }
}
