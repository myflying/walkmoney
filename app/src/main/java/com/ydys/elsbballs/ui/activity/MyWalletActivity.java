package com.ydys.elsbballs.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaeger.library.StatusBarUtil;
import com.ydys.elsbballs.App;
import com.ydys.elsbballs.R;
import com.ydys.elsbballs.bean.WalletInfoRet;
import com.ydys.elsbballs.common.Constants;
import com.ydys.elsbballs.presenter.MyWalletInfoPresenterImp;
import com.ydys.elsbballs.presenter.Presenter;
import com.ydys.elsbballs.ui.adapter.GoldDayAdapter;
import com.ydys.elsbballs.view.MyWalletInfoView;

import butterknife.BindView;
import butterknife.OnClick;

public class MyWalletActivity extends BaseActivity implements MyWalletInfoView {

    @BindView(R.id.gold_day_list_view)
    RecyclerView mGoldDayListView;

    @BindView(R.id.tv_gold_num)
    TextView mGoldNumTv;

    @BindView(R.id.tv_money)
    TextView mMoneyTv;

    @BindView(R.id.tv_today_gold_num)
    TextView mTodayGoldNumTv;

    @BindView(R.id.tv_total_gold_num)
    TextView mTotalGoldNumTv;

    @BindView(R.id.tv_exchange_desc)
    TextView mExchangeTv;

    GoldDayAdapter goldDayAdapter;

    MyWalletInfoPresenterImp myWalletInfoPresenterImp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        myWalletInfoPresenterImp = new MyWalletInfoPresenterImp(this, this);

        mGoldDayListView.setLayoutManager(new LinearLayoutManager(this));
        goldDayAdapter = new GoldDayAdapter(this, null);

        mGoldDayListView.setLayoutManager(new LinearLayoutManager(this));
        mGoldDayListView.setAdapter(goldDayAdapter);
        myWalletInfoPresenterImp.loadWalletInfo(App.mUserInfo != null ? App.mUserInfo.getId() : "");
    }

    @OnClick(R.id.btn_cash_money)
    void cashNow() {
        Intent intent = new Intent(this, CashActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_cash_record)
    void cashRecord() {
        Intent intent = new Intent(this, CashRecordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(WalletInfoRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (tData.getData() != null) {
                goldDayAdapter.setNewData(tData.getData().getList());

                mGoldNumTv.setText(tData.getData().getGold() + "");
                mMoneyTv.setText("约" + tData.getData().getMoney() + "元");
                mTodayGoldNumTv.setText(tData.getData().getToDayGold() + "");
                mTotalGoldNumTv.setText(tData.getData().getCountGold() + "");
                mExchangeTv.setText(tData.getData().getExchangeRate() + "金币=1元");
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}
