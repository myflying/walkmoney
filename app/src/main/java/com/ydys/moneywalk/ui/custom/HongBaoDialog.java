package com.ydys.moneywalk.ui.custom;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.ydys.moneywalk.R;


public class HongBaoDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    ImageView mHongBaoGoldBgImage;

    ImageView mHongBaoBgIv;

    ImageView mOpenHbIv;

    LinearLayout mOpenInfoLayout;

    LinearLayout mHongBaoWinLayout;

    Button mStartStepBtn;

    TextView mNewGoldNumTv;

    public HongBaoDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public HongBaoDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hong_bao_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mHongBaoGoldBgImage = findViewById(R.id.iv_hong_bao_gold_bg);
        mHongBaoBgIv = findViewById(R.id.iv_hb_bg);
        mOpenInfoLayout = findViewById(R.id.layout_open_info);
        mHongBaoWinLayout = findViewById(R.id.layout_win_gold);
        mStartStepBtn = findViewById(R.id.btn_start_step);
        mNewGoldNumTv = findViewById(R.id.tv_new_gold_num);

        mOpenHbIv = findViewById(R.id.iv_open_hb);
        setCanceledOnTouchOutside(false);

        mOpenHbIv.setOnClickListener(this);
        mStartStepBtn.setOnClickListener(this);

        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_anim);
        operatingAnim.setInterpolator(new LinearInterpolator()); // 设置插入器);

        if (operatingAnim != null) {
            mHongBaoGoldBgImage.startAnimation(operatingAnim);
        } else {
            mHongBaoGoldBgImage.setAnimation(operatingAnim);
            mHongBaoGoldBgImage.startAnimation(operatingAnim);
        }

        setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }

    //自动开启红包
    public void autoOpenHongBao(int newGoldNum) {
        mNewGoldNumTv.setText(newGoldNum + "");

        mHongBaoBgIv.setImageResource(R.mipmap.hong_bao_open_bg);
        mOpenInfoLayout.setVisibility(View.GONE);
        mHongBaoWinLayout.setVisibility(View.VISIBLE);
        setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return false;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_hb:
                mHongBaoBgIv.setImageResource(R.mipmap.hong_bao_open_bg);
                mOpenInfoLayout.setVisibility(View.GONE);
                mHongBaoWinLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_start_step:
                dismiss();
                break;
            default:
                break;
        }
    }
}