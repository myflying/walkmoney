package com.ydys.moneywalk.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.util.RandomUtils;


public class ReceiveGoldDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    ImageView mReceiveGoldIv;

    TextView mGoldNumTv;

    TextView mTotalGoldNumTv;

    TextView mMoneyTv;

    TextView mCountDownTv;

    LinearLayout mCountDownLayout;

    ImageView mCloseIv;

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
        mReceiveGoldIv = findViewById(R.id.iv_receive_gold_bg);
        mGoldNumTv = findViewById(R.id.tv_gold_num);
        mTotalGoldNumTv = findViewById(R.id.tv_total_gold_num);
        mMoneyTv = findViewById(R.id.tv_money);

        mCountDownTv = findViewById(R.id.tv_count_down);
        mCountDownLayout = findViewById(R.id.layout_count_down);
        mCloseIv = findViewById(R.id.iv_close);

        setCanceledOnTouchOutside(false);

        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_gold_anim);
        operatingAnim.setInterpolator(new LinearInterpolator()); // 设置插入器);

        if (operatingAnim != null) {
            mReceiveGoldIv.startAnimation(operatingAnim);
        } else {
            mReceiveGoldIv.setAnimation(operatingAnim);
            mReceiveGoldIv.startAnimation(operatingAnim);
        }

        mCloseIv.setVisibility(View.GONE);
        mCountDownLayout.setVisibility(View.VISIBLE);

        new CountDownTimer(4 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Logger.i("广告倒计时--->" + millisUntilFinished / 1000);
                mCountDownTv.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                mCountDownLayout.setVisibility(View.GONE);
                mCloseIv.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void updateGoldInfo(String gold, String totalGold, String money) {
        mGoldNumTv.setText(gold + "金币");
        mTotalGoldNumTv.setText(totalGold);
        mMoneyTv.setText(money);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}