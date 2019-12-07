package com.ydys.moneywalk.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.CashRecordInfo;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.util.MatrixUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;

public class CashDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.tv_cash_title)
    TextView mCashTitleTv;

    @BindView(R.id.tv_cash_money)
    TextView mCashMoneyTv;

    @BindView(R.id.tv_state)
    TextView mCashStateTv;

    @BindView(R.id.tv_cash_date)
    TextView mCashDateTv;

    @BindView(R.id.tv_finish_date)
    TextView mFinishDateTv;

    @BindView(R.id.tv_cash_order_num)
    TextView mOrderNumTv;

    @BindView(R.id.tv_qq_qun)
    TextView mQQQunTv;

    CashRecordInfo cashRecordInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash_detail;
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
        mTitleTv.setText("提现详情");

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cashRecordInfo = (CashRecordInfo) bundle.getSerializable("cash_record_info");
        }
        if (cashRecordInfo != null) {
            mCashTitleTv.setText("微信" + MatrixUtils.getPrecisionMoney(cashRecordInfo.getMoney()) + "元提现");
            mCashMoneyTv.setText("+" + MatrixUtils.getPrecisionMoney(cashRecordInfo.getMoney()));
            mCashStateTv.setText(cashRecordInfo.getStatus() == 1 ? "已到账" : "审核中");
            mCashDateTv.setText(cashRecordInfo.getAddTime());
            mFinishDateTv.setText(cashRecordInfo.getFinishTime());
            mOrderNumTv.setText(cashRecordInfo.getOrderSn());
            mQQQunTv.setText(App.initInfo != null ? App.initInfo.getAppConfig().getMobile() + "" : "");
        }
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

}
