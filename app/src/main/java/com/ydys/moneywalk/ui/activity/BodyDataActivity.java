package com.ydys.moneywalk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.BodyInfo;
import com.ydys.moneywalk.bean.BodyInfoRet;
import com.ydys.moneywalk.bean.MessageEvent;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.BodyInfoPresenterImp;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.util.MatrixUtils;
import com.ydys.moneywalk.view.BodyInfoView;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BodyDataActivity extends BaseActivity implements View.OnClickListener, BodyInfoView {

    BodyInfoPresenterImp bodyInfoPresenterImp;

    BottomSheetDialog commonDialog;

    View sexView;

    WheelView sexWheelView;

    TextView mSexConfigTv;

    TextView mSexCancelTv;

    WheelView.WheelViewStyle wheelViewStyle;

    String[] files = {"sex", "age", "height", "weight", "target_step"};

    int chooseIndex;

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.tv_sex)
    TextView mSexTv;

    @BindView(R.id.tv_age)
    TextView mAgeTv;

    @BindView(R.id.tv_height)
    TextView mHeightTv;

    @BindView(R.id.tv_weight)
    TextView mWeightTv;

    @BindView(R.id.tv_step_num)
    TextView mStepNumTv;

    @BindView(R.id.tv_bmi_data)
    TextView mBmiDataTv;

    private BodyInfo bodyInfo;

    private boolean isUpdate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_body_data;
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
        mTitleTv.setText("身体数据");

        wheelViewStyle = new WheelView.WheelViewStyle();
        wheelViewStyle.textSize = 16;
        wheelViewStyle.selectedTextSize = 16;
        wheelViewStyle.holoBorderColor = ContextCompat.getColor(this, R.color.transparent);
        wheelViewStyle.selectedTextColor = ContextCompat.getColor(this, R.color.privacy_text_color);
    }

    public void showDialog(int type, List<String> list, int sindex) {
        sexView = LayoutInflater.from(this).inflate(R.layout.choose_sex_view, null);
        sexWheelView = sexView.findViewById(R.id.wheel_view_sex);
        mSexConfigTv = sexView.findViewById(R.id.tv_sex_config);
        mSexCancelTv = sexView.findViewById(R.id.tv_sex_cancel);

        mSexConfigTv.setOnClickListener(this);
        mSexCancelTv.setOnClickListener(this);

        sexWheelView.setWheelData(list);
        sexWheelView.setWheelAdapter(new ArrayWheelAdapter(this));
        sexWheelView.setLoop(false);
        sexWheelView.setStyle(wheelViewStyle);
        sexWheelView.setSelection(sindex);
        sexWheelView.setSkin(WheelView.Skin.Holo);
        commonDialog = new BottomSheetDialog(this);
        commonDialog.setContentView(sexView);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        bodyInfoPresenterImp = new BodyInfoPresenterImp(this, this);
        bodyInfoPresenterImp.getBodyInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "");
    }

    @OnClick(R.id.layout_sex)
    void chooseSex() {
        chooseIndex = 0;
        List<String> sexList = new ArrayList<>();
        sexList.add("男");
        sexList.add("女");
        showDialog(chooseIndex, sexList, 0);

        if (commonDialog != null && !commonDialog.isShowing()) {
            commonDialog.show();
        }
    }

    @OnClick(R.id.layout_age)
    void chooseAge() {
        chooseIndex = 1;
        List<String> ageList = new ArrayList<>();
        for (int i = 5; i < 100; i++) {
            ageList.add(i + "");
        }
        showDialog(chooseIndex, ageList, 20);

        if (commonDialog != null && !commonDialog.isShowing()) {
            commonDialog.show();
        }
    }

    @OnClick(R.id.layout_sg)
    void chooseHeight() {
        chooseIndex = 2;
        List<String> heightList = new ArrayList<>();
        for (int i = 100; i < 220; i++) {
            heightList.add(i + "厘米");
        }
        showDialog(chooseIndex, heightList, 70);

        if (commonDialog != null && !commonDialog.isShowing()) {
            commonDialog.show();
        }
    }


    @OnClick(R.id.layout_tz)
    void chooseWeight() {
        chooseIndex = 3;
        List<String> weightList = new ArrayList<>();
        for (int i = 10; i < 200; i++) {
            weightList.add(i + "kg");
        }
        showDialog(chooseIndex, weightList, 55);

        if (commonDialog != null && !commonDialog.isShowing()) {
            commonDialog.show();
        }
    }

    @OnClick(R.id.layout_step_num)
    void chooseStepNum() {
        chooseIndex = 4;
        List<String> weightList = new ArrayList<>();
        for (int i = 2000; i < 30000; i += 1000) {
            weightList.add(i + "");
        }
        showDialog(chooseIndex, weightList, 8);

        if (commonDialog != null && !commonDialog.isShowing()) {
            commonDialog.show();
        }
    }

    @OnClick(R.id.layout_bmi)
    void bmiData() {
        if (bodyInfo.getHeight() == 0) {
            ToastUtils.showLong("请先填写身高");
            return;
        }
        if (bodyInfo.getWeight() == 0) {
            ToastUtils.showLong("请先填写身高");
            return;
        }

        Intent intent = new Intent(this, BMIDataActivity.class);
        intent.putExtra("bmi_height", bodyInfo.getHeight());
        intent.putExtra("bmi_weight", bodyInfo.getWeight());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sex_config:
                String tempStr = sexWheelView.getSelectionItem().toString();
                Logger.i("choose txt--->" + tempStr + "---selection--->" + sexWheelView.getSelection());
                if (commonDialog != null && commonDialog.isShowing()) {
                    commonDialog.dismiss();
                }
                switch (chooseIndex) {
                    case 0:
                        //mSexTv.setText(tempStr);
                        tempStr = "男".equals(tempStr) ? "1" : "2";
                        break;
                    case 1:
                        //mAgeTv.setText(tempStr);
                        break;
                    case 2:
                        tempStr = tempStr.substring(0, tempStr.indexOf("厘米"));
                        //mHeightTv.setText(tempStr);
                        break;
                    case 3:
                        tempStr = tempStr.substring(0, tempStr.indexOf("kg"));
                        //mWeightTv.setText(tempStr);
                        break;
                    case 4:
                        //mStepNumTv.setText(tempStr);
                        break;
                    default:
                        break;
                }
                isUpdate = true;

                bodyInfoPresenterImp.updateBodyInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "", files[chooseIndex], tempStr);
                break;
            case R.id.tv_sex_cancel:
                if (commonDialog != null && commonDialog.isShowing()) {
                    commonDialog.dismiss();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(BodyInfoRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            bodyInfo = tData.getData();
            Logger.i(JSON.toJSONString(bodyInfo));

            if (isUpdate) {
                ToastUtils.showLong("保存成功");
            }
            if (tData != null) {
                String sex = "未知";
                if (bodyInfo.getSex() > 0) {
                    sex = bodyInfo.getSex() == 1 ? "男" : "女";
                }
                mSexTv.setText(sex);
                mAgeTv.setText(bodyInfo.getAge() == 0 ? "" : bodyInfo.getAge() + "");
                mHeightTv.setText(bodyInfo.getHeight() == 0 ? "" : bodyInfo.getHeight() + "厘米");
                mWeightTv.setText(bodyInfo.getWeight() == 0 ? "" : bodyInfo.getWeight() + "kg");
                mStepNumTv.setText(bodyInfo.getTarget_step() == 0 ? "" : bodyInfo.getTarget_step() + "");

                if (bodyInfo.getHeight() > 0 && bodyInfo.getWeight() > 0) {
                    double bmiData = (double) bodyInfo.getWeight() / (((double) bodyInfo.getHeight() / 100) * ((double) bodyInfo.getHeight() / 100));
                    mBmiDataTv.setText(MatrixUtils.getPrecisionMoney(bmiData));
                }

                if (bodyInfo.getWeight() > 0 && App.initInfo != null) {
                    App.initInfo.getUserStepData().setWeight(bodyInfo.getWeight());

                    MessageEvent messageEvent = new MessageEvent("update_weight");
                    EventBus.getDefault().post(messageEvent);
                }
            }
        } else {
            if (isUpdate) {
                ToastUtils.showLong("保存失败");
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        ToastUtils.showLong("系统错误");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    @OnClick(R.id.iv_back)
    void back() {
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }
}
