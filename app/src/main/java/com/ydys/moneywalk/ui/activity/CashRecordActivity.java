package com.ydys.moneywalk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.CashRecordInfo;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.adapter.CashRecordAdapter;
import com.ydys.moneywalk.ui.custom.NormalDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CashRecordActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.cash_record_list_view)
    RecyclerView mCashRecordListView;

    CashRecordAdapter cashRecordAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash_record;
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
        mTitleTv.setText("提现记录");

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        List<CashRecordInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CashRecordInfo cashRecordInfo = new CashRecordInfo();
            cashRecordInfo.setCashMoney(1.2);
            cashRecordInfo.setCashState(1);
            cashRecordInfo.setCashType(1);
            list.add(cashRecordInfo);
        }
        cashRecordAdapter = new CashRecordAdapter(this, list);

        mCashRecordListView.setLayoutManager(new LinearLayoutManager(this));
        mCashRecordListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(this, R.color.line1_color), 1));
        mCashRecordListView.setAdapter(cashRecordAdapter);

        View headView = new View(this);
        headView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(12)));
        headView.setBackgroundColor(ContextCompat.getColor(this, R.color.line1_color));
        cashRecordAdapter.addHeaderView(headView);

        cashRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(CashRecordActivity.this, CashDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

}
