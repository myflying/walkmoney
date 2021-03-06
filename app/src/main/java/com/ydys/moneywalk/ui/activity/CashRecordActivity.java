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
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.CashRecordInfo;
import com.ydys.moneywalk.bean.CashRecordInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.CashRecordInfoPresenterImp;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.adapter.CashRecordAdapter;
import com.ydys.moneywalk.ui.custom.NormalDecoration;
import com.ydys.moneywalk.view.CashRecordInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CashRecordActivity extends BaseActivity implements CashRecordInfoView {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.cash_record_list_view)
    RecyclerView mCashRecordListView;


    @BindView(R.id.layout_no_data)
    LinearLayout mNoDataLayout;

    CashRecordAdapter cashRecordAdapter;

    int currentPage = 1;

    int pageSize = 10;

    CashRecordInfoPresenterImp cashRecordInfoPresenterImp;

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
        cashRecordInfoPresenterImp = new CashRecordInfoPresenterImp(this, this);

        cashRecordAdapter = new CashRecordAdapter(this, null);
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("cash_record_info", cashRecordAdapter.getData().get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        cashRecordAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                currentPage++;
                cashRecordInfoPresenterImp.cashRecordList(App.mUserInfo != null ? App.mUserInfo.getId() : "", currentPage);
            }
        }, mCashRecordListView);

        cashRecordInfoPresenterImp.cashRecordList(App.mUserInfo != null ? App.mUserInfo.getId() : "", currentPage);
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
    public void loadDataSuccess(CashRecordInfoRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            if (currentPage == 1) {
                if (tData.getData() != null && tData.getData().size() > 0) {
                    cashRecordAdapter.setNewData(tData.getData());
                } else {
                    mCashRecordListView.setVisibility(View.GONE);
                    mNoDataLayout.setVisibility(View.VISIBLE);
                }
            } else {
                cashRecordAdapter.addData(tData.getData());
            }

            if (tData.getData() != null && tData.getData().size() == pageSize) {
                cashRecordAdapter.loadMoreComplete();
            } else {
                cashRecordAdapter.loadMoreEnd();
            }
        } else {
            if (currentPage == 1) {
                mCashRecordListView.setVisibility(View.GONE);
                mNoDataLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (currentPage == 1) {
            mCashRecordListView.setVisibility(View.GONE);
            mNoDataLayout.setVisibility(View.VISIBLE);
        }
    }
}
