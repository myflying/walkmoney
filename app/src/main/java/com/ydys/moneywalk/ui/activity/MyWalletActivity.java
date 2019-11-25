package com.ydys.moneywalk.ui.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.GoldDayInfo;
import com.ydys.moneywalk.bean.GoldDetailInfo;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.adapter.GoldDayAdapter;
import com.ydys.moneywalk.ui.custom.ActRuleDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyWalletActivity extends BaseActivity {

    @BindView(R.id.gold_day_list_view)
    RecyclerView mGoldDayListView;

    GoldDayAdapter goldDayAdapter;

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
        List<GoldDayInfo> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            GoldDayInfo goldDayInfo = new GoldDayInfo();
            goldDayInfo.setGoldDate("今天");
            List<GoldDetailInfo> detailList = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                GoldDetailInfo goldDetailInfo = new GoldDetailInfo();
                goldDetailInfo.setGoldNum(1000 + (i + j + 1));
                detailList.add(goldDetailInfo);
            }
            goldDayInfo.setDetailList(detailList);

            list.add(goldDayInfo);
        }

        mGoldDayListView.setLayoutManager(new LinearLayoutManager(this));
        goldDayAdapter = new GoldDayAdapter(this, list);
        mGoldDayListView.setLayoutManager(new LinearLayoutManager(this));
        mGoldDayListView.setAdapter(goldDayAdapter);
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }
}
