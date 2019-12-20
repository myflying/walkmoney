package com.ydys.elsbballs.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydys.elsbballs.R;

/**
 * 加载中
 */
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
        AnimationDrawable animationDrawable;
        animationDrawable = (AnimationDrawable) mLoadingIv.getBackground();
        animationDrawable.start();
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