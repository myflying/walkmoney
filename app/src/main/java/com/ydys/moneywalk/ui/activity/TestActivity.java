package com.ydys.moneywalk.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.presenter.UserInfoPresenterImp;
import com.ydys.moneywalk.ui.custom.StepNumProgressView;
import com.ydys.moneywalk.view.UserInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TestActivity extends BaseActivity implements UserInfoView {

//    @BindView(R.id.progress_bar)
//    ArcProgressBar mArcProgressBar;

//    @BindView(R.id.cir_view)
//    CircleView circleView;

//    @BindView(R.id.round_pr)
//    RoundProgressBar roundProgressBar;

    //    @BindView(R.id.ctv)
//    CircleTimeView circleTimeView;
    @BindView(R.id.temp_view)
    StepNumProgressView mDeviceTempHum;

    @BindView(R.id.layout_gold1)
    FrameLayout mGold1Layout;

    @BindView(R.id.layout_gold2)
    FrameLayout mGold2Layout;

    @BindView(R.id.layout_gold3)
    FrameLayout mGold3Layout;

    @BindView(R.id.layout_gold4)
    FrameLayout mGold4Layout;

    private Handler mHandler = new Handler();

    int count = 0;

    UserInfoPresenterImp userInfoPresenterImp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                count += 5;
//                Logger.i("count--->" + count);
//                roundProgressBar.setProgress(count);
//                mHandler.postDelayed(this, 100);
//            }
//        }, 200);
        //circleTimeView.startTimer();
        mDeviceTempHum.setStepProgress(20);

        List<Animator> animators = new ArrayList<>();
        ObjectAnimator translationYAnim1 = ObjectAnimator.ofFloat(mGold1Layout, "translationY", -12.0f, 12.0f, -12.0f);
        translationYAnim1.setDuration(2500);
        translationYAnim1.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim1.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim1);

        ObjectAnimator translationYAnim2 = ObjectAnimator.ofFloat(mGold2Layout, "translationY", -10.0f, 10.0f, -10.0f);
        translationYAnim2.setDuration(2200);
        translationYAnim2.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim2.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim2);

        ObjectAnimator translationYAnim3 = ObjectAnimator.ofFloat(mGold3Layout, "translationY", -12.0f, 12.0f, -12.0f);
        translationYAnim3.setDuration(2000);
        translationYAnim3.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim3.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim3);

        ObjectAnimator translationYAnim4 = ObjectAnimator.ofFloat(mGold4Layout, "translationY", -11.0f, 11.0f, -11.0f);
        translationYAnim4.setDuration(2500);
        translationYAnim4.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim4.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim4);

        AnimatorSet btnSexAnimatorSet = new AnimatorSet();
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.setStartDelay(100);
        btnSexAnimatorSet.start();

        userInfoPresenterImp = new UserInfoPresenterImp(this, this);

    }

    public void jian() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (count > 0) {
                    count--;
                    Logger.i("count--->" + count);
                    mDeviceTempHum.setStepProgress(count);
                    mHandler.postDelayed(this, 1);
                }
            }
        }, 1);
    }

    @OnClick(R.id.btn_test)
    void test() {
        //userInfoPresenterImp.login("zz123123", "测试用户");
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (count < 60) {
//                    count++;
//                    Logger.i("count--->" + count);
//                    mDeviceTempHum.setStepProgress(count);
//                    mHandler.postDelayed(this, 1);
//                } else {
//                    jian();
//                }
//            }
//        }, 1);

//        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                mArcProgressBar.setProgress((int) animation.getAnimatedValue());
//            }
//        });
//        valueAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                mArcProgressBar.setProgressDesc("已售罄");
//            }
//        });
//        valueAnimator.setDuration(5000);
//        valueAnimator.start();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(UserInfoRet tData) {
        Logger.i("login res--->" + JSON.toJSONString(tData));
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
