package com.ydys.elsbballs.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.ydys.elsbballs.R;


public class HongBaoDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    ImageView mHongBaoBgIv;

    ImageView mOpenHbIv;

    LinearLayout mOpenInfoLayout;

    LinearLayout mHongBaoWinLayout;

    LinearLayout mNewPerLayout;

    LinearLayout mLoginLayout;

    ImageView mStartStepBtn;

    ImageView mLoginVideo;

    TextView mNewGoldNumTv;

    ImageView mCloseIv;

    TextView mTitleTv;

    TextView mSubTitleTv;

    String newUserMoney;

    public OpenHBListener openHBListener;

    private FrameAnimation mFrameAnimation;

    private int[] mImgResIds = new int[]{
            R.mipmap.open_hb_01,
            R.mipmap.open_hb_02,
            R.mipmap.open_hb_03,
            R.mipmap.open_hb_04,
            R.mipmap.open_hb_05,
            R.mipmap.open_hb_06,
            R.mipmap.open_hb_07,
            R.mipmap.open_hb_08,
            R.mipmap.open_hb_09,
            R.mipmap.open_hb_10,
            R.mipmap.open_hb_11
    };

    public void setOpenHBListener(OpenHBListener openHBListener) {
        this.openHBListener = openHBListener;
    }

    public interface OpenHBListener {
        void openHongBao(int type);

        void closeHongBao();

        void startMakeMoney();
    }

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
        mHongBaoBgIv = findViewById(R.id.iv_hb_bg);
        mOpenInfoLayout = findViewById(R.id.layout_open_info);
        mHongBaoWinLayout = findViewById(R.id.layout_win_gold);
        mStartStepBtn = findViewById(R.id.btn_start_step);
        mLoginVideo = findViewById(R.id.iv_login_video);
        mNewGoldNumTv = findViewById(R.id.tv_new_gold_num);
        mCloseIv = findViewById(R.id.iv_close);
        mNewPerLayout = findViewById(R.id.layout_new_person);
        mLoginLayout = findViewById(R.id.layout_login);

        mTitleTv = findViewById(R.id.tv_title_txt);
        mSubTitleTv = findViewById(R.id.tv_sub_title_txt);

        mOpenHbIv = findViewById(R.id.iv_open_hb);
        setCanceledOnTouchOutside(false);

        mOpenInfoLayout.setOnClickListener(this);
        mHongBaoWinLayout.setOnClickListener(this);
        mOpenHbIv.setOnClickListener(this);
        mStartStepBtn.setOnClickListener(this);
        mLoginVideo.setOnClickListener(this);
        mCloseIv.setOnClickListener(this);
        mLoginLayout.setOnClickListener(this);
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

    public void startAnim() {
        mFrameAnimation = new FrameAnimation(mOpenHbIv, mImgResIds, 125, false);
        mFrameAnimation.setAnimationListener(new FrameAnimation.AnimationListener() {
            @Override
            public void onAnimationStart() {
                Logger.i("hb open start--->");
            }

            @Override
            public void onAnimationEnd() {
                Logger.i("hb open end--->");
                autoOpenHongBao(newUserMoney);
            }

            @Override
            public void onAnimationRepeat() {
                Logger.i("hb open start--->");
            }
        });
    }

    public void updateDialogInfo(int type, String money) {
        newUserMoney = money;
        mCloseIv.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
        mNewPerLayout.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
        mLoginLayout.setVisibility(type == 1 ? View.GONE : View.VISIBLE);
    }

    //自动开启红包
    public void autoOpenHongBao(String newGoldNum) {
        mNewGoldNumTv.setText(newGoldNum);

        mHongBaoBgIv.setImageResource(R.mipmap.new_per_opend);
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

    public void stopAnim() {
        if (mFrameAnimation != null) {
            mFrameAnimation.release();
            mFrameAnimation = null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_open_hb:
            case R.id.layout_open_info:
                openHBListener.openHongBao(1);
                //dismiss();
                if (mFrameAnimation != null) {
                    //如果正在转动，则直接返回
                    return;
                }
                startAnim();
                break;
            case R.id.layout_win_gold:
            case R.id.btn_start_step:
                openHBListener.startMakeMoney();
                dismiss();
                break;
            case R.id.layout_login:
            case R.id.iv_login_video:
                openHBListener.openHongBao(2);
                dismiss();
                break;
            case R.id.iv_close:
                stopAnim();
                openHBListener.closeHongBao();
                dismiss();
                break;
            default:
                break;
        }
    }
}