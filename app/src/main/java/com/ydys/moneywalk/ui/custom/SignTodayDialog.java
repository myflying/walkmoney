package com.ydys.moneywalk.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydys.moneywalk.R;


public class SignTodayDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    ImageView mReceiveGoldIv;

    ImageView mCloseIv;

    TextView mSignDayNumTv;

    TextView mSignDayGoldTv;

    TextView mTotalGoldNumTv;

    TextView mMoneyTv;

    LinearLayout mDoubleGoldLayout;

    public interface SignDayListener{
        void doubleGoldVideo();
    }

    public SignDayListener signDayListener;

    public void setSignDayListener(SignDayListener signDayListener) {
        this.signDayListener = signDayListener;
    }

    public SignTodayDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public SignTodayDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_today_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mReceiveGoldIv = findViewById(R.id.iv_receive_gold_bg);
        mCloseIv = findViewById(R.id.iv_close);
        mSignDayNumTv = findViewById(R.id.tv_sign_day_num);
        mSignDayGoldTv = findViewById(R.id.tv_sign_day_gold);
        mTotalGoldNumTv = findViewById(R.id.tv_total_gold_num);
        mDoubleGoldLayout = findViewById(R.id.layout_double_gold);
        mMoneyTv = findViewById(R.id.tv_my_money);

        mCloseIv.setOnClickListener(this);
        mDoubleGoldLayout.setOnClickListener(this);

        setCanceledOnTouchOutside(false);

        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_gold_anim);
        operatingAnim.setInterpolator(new LinearInterpolator()); // 设置插入器);

        if (operatingAnim != null) {
            mReceiveGoldIv.startAnimation(operatingAnim);
        } else {
            mReceiveGoldIv.setAnimation(operatingAnim);
            mReceiveGoldIv.startAnimation(operatingAnim);
        }
    }

    public void updateTodayInfo(String day, String gold, String totalGold, String money) {
        mSignDayNumTv.setText(day);
        mSignDayGoldTv.setText(gold + "金币");
        mTotalGoldNumTv.setText(totalGold);
        mMoneyTv.setText("≈" + money + "元");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_double_gold:
                signDayListener.doubleGoldVideo();
                break;
            case R.id.iv_close:
                dismiss();
                break;
            default:
                break;
        }
    }
}