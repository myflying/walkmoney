package com.ydys.moneywalk.ui.custom;

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

    FrameLayout mCountDownLayout;

    TextView mCountDownTv;

    ImageView mCloseIv;

    CountDownTimer countDownTimer;

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
        mReceiveGoldIv = findViewById(R.id.iv_receive_gold_bg);
        mGoldNumTv = findViewById(R.id.tv_gold_num);
        mTotalGoldNumTv = findViewById(R.id.tv_total_gold_num);
        mMoneyTv = findViewById(R.id.tv_money);

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
    }

    public void updateGoldInfo(String gold, String totalGold, String money) {
        mGoldNumTv.setText(gold + "金币");
        mTotalGoldNumTv.setText(totalGold);
        mMoneyTv.setText(money);
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