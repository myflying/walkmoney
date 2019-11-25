package com.ydys.moneywalk.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.ydys.moneywalk.R;


public class ReceiveGoldDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    ImageView mReceiveGoldIv;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }
}