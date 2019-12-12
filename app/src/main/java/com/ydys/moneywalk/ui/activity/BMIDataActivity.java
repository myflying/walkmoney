package com.ydys.moneywalk.ui.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.presenter.Presenter;

import butterknife.BindView;
import butterknife.OnClick;

public class BMIDataActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.iv_sign)
    ImageView mSignIv;

    @BindView(R.id.tv_bmi_desc)
    TextView mBmiDescTv;

    double bmiHeight;

    double bmiWeight;

    private double bmiData;

    private double space;

    private double totalWidth = 920;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bmi_data;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void initVars() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 0);
    }

    @Override
    protected void initViews() {
        mTitleTv.setTextColor(ContextCompat.getColor(this, R.color.black));
        mTitleTv.setText("BMI");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bmiHeight = bundle.getInt("bmi_height");
            bmiWeight = bundle.getInt("bmi_weight");
        }

        try {
            bmiData = bmiWeight / ((bmiHeight / 100) * (bmiHeight / 100));
            Logger.i("bmi data--->" + bmiData);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        space = totalWidth / 4;
        int xWidth = 0;
        if (bmiData < 18.5) {
            mBmiDescTv.setText("偏瘦");
            xWidth = (int) ((space / 18.5) * bmiData);
        } else if (bmiData >= 18.5 && bmiData < 24) {
            mBmiDescTv.setText("理想");
            xWidth = (int) ((space / (24 - 18.5)) * (bmiData - 18.5)) + (int) space;
        } else if (bmiData >= 24 && bmiData < 28) {
            mBmiDescTv.setText("偏胖");
            xWidth = (int) ((space / (28 - 24)) * (bmiData - 24)) + (int) (space * 2);
        } else if (bmiData >= 28 && bmiData < 35) {
            mBmiDescTv.setText("肥胖");
            xWidth = (int) ((space / (35 - 24)) * (bmiData - 28)) + (int) (space * 3);
        } else {
            mBmiDescTv.setText("超胖");
            xWidth = (int) totalWidth;
        }

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(xWidth, 0, 0, 0);
        mSignIv.setLayoutParams(params);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }
}
