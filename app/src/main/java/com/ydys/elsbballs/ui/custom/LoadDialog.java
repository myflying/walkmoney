package com.ydys.elsbballs.ui.custom;

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

import com.ydys.elsbballs.R;


public class LoadDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    ImageView mLoadingIv;

    public LoadDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LoadDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mLoadingIv = findViewById(R.id.iv_loading);
        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_gold_anim);
        operatingAnim.setInterpolator(new LinearInterpolator()); // 设置插入器);

        if (operatingAnim != null) {
            mLoadingIv.startAnimation(operatingAnim);
        } else {
            mLoadingIv.setAnimation(operatingAnim);
            mLoadingIv.startAnimation(operatingAnim);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_config:
                break;
            default:
                break;
        }
    }
}