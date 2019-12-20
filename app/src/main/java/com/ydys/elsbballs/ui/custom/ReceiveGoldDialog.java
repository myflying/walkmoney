package com.ydys.elsbballs.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ydys.elsbballs.R;


public class ReceiveGoldDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    RoundRelativeLayout mAdLayout;

    ImageView mReceiveGoldIv;

    TextView mGoldNumTv;

    TextView mTotalGoldNumTv;

    TextView mMoneyTv;

    FrameLayout mCountDownLayout;

    TextView mCountDownTv;

    ImageView mCloseIv;

    CountDownTimer countDownTimer;

    //ImageView mBannerBgIv;

    ImageView mGoldTitleIv;

    TextView mReceiveTitleTv;

    public interface GoldDialogListener {
        void closeGoldDialog();
    }

    public GoldDialogListener goldDialogListener;

    public void setGoldDialogListener(GoldDialogListener goldDialogListener) {
        this.goldDialogListener = goldDialogListener;
    }

    public ReceiveGoldDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ReceiveGoldDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_gold_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        //广告位
        mAdLayout = findViewById(R.id.express_container);
       // mBannerBgIv = findViewById(R.id.iv_banner_bg);

        mReceiveGoldIv = findViewById(R.id.iv_receive_gold_bg);
        mGoldNumTv = findViewById(R.id.tv_gold_num);
        mTotalGoldNumTv = findViewById(R.id.tv_total_gold_num);
        mMoneyTv = findViewById(R.id.tv_money);
        mGoldTitleIv = findViewById(R.id.iv_gold_title);
        mReceiveTitleTv = findViewById(R.id.tv_receive_title);

        mCountDownLayout = findViewById(R.id.layout_count_down);
        mCountDownTv = findViewById(R.id.tv_count_down);
        mCloseIv = findViewById(R.id.iv_close);
        mCountDownLayout.setOnClickListener(this);
        mCountDownLayout.setClickable(false);
        setCanceledOnTouchOutside(false);
        setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_gold_anim);
        operatingAnim.setInterpolator(new LinearInterpolator()); // 设置插入器);

        if (operatingAnim != null) {
            mReceiveGoldIv.startAnimation(operatingAnim);
        } else {
            mReceiveGoldIv.setAnimation(operatingAnim);
            mReceiveGoldIv.startAnimation(operatingAnim);
        }

        countDownTimer = new CountDownTimer(3 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Logger.i("广告倒计时--->" + millisUntilFinished / 1000);
                mCountDownTv.setText(((int) (millisUntilFinished / 1000) + 1) + "");
            }

            @Override
            public void onFinish() {
                mCountDownLayout.setClickable(true);
                mCountDownTv.setVisibility(View.GONE);
                mCloseIv.setVisibility(View.VISIBLE);
            }
        }.start();

//        //获取背景，并将其强转成AnimationDrawable
//        AnimationDrawable animationDrawable = (AnimationDrawable) mBannerBgIv.getBackground();
//        //判断是否在运行
//        if (!animationDrawable.isRunning()) {
//            //开启帧动画
//            animationDrawable.start();
//        }
    }

    public void updateGoldInfo(String gold, String totalGold, String money, View adView) {
        mGoldNumTv.setText(gold + "金币");
        mTotalGoldNumTv.setText(totalGold);
        mMoneyTv.setText(money);
        if (adView != null) {
            mAdLayout.addView(adView);
        }
    }

    public void updateCommonInfo(String gold, String totalGold, String money, View adView,int type) {
        mGoldNumTv.setText(gold + "金币");
        mTotalGoldNumTv.setText(totalGold);
        mMoneyTv.setText(money);

        mReceiveTitleTv.setText(type == 1 ? "恭喜获得" : "获得通关奖励");
        mGoldTitleIv.setImageResource(type == 1 ? R.mipmap.receive_top : R.mipmap.game_pass_bg);
        if (adView != null) {
            mAdLayout.addView(adView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_count_down:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
                mCountDownTv.setVisibility(View.VISIBLE);
                mCloseIv.setVisibility(View.GONE);
                goldDialogListener.closeGoldDialog();
                break;
            default:
                break;
        }
    }
}