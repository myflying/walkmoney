package com.ydys.moneywalk.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.CashMoneyInfo;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.adapter.CashMoneyAdapter;
import com.ydys.moneywalk.ui.custom.LoginOutDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现页面
 */
public class CashActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.cash_money_list_view)
    RecyclerView mCashMoneyListView;

    CashMoneyAdapter cashMoneyAdapter;

    private int lastIndex;

    LoginOutDialog loginOutDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash;
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
        mTitleTv.setText("提现");

        loginOutDialog = new LoginOutDialog(this, R.style.common_dialog);
        //loginOutDialog.setLoginOutListener(this);
        List<CashMoneyInfo> list = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            CashMoneyInfo cashMoneyInfo = new CashMoneyInfo();
            cashMoneyInfo.setCashMoney((i + 1));
            cashMoneyInfo.setGoldNum((i + 1) * 1000);
            if (i == 0) {
                cashMoneyInfo.setSelected(true);
            }
            list.add(cashMoneyInfo);
        }

        cashMoneyAdapter = new CashMoneyAdapter(this, list);
        mCashMoneyListView.setLayoutManager(new GridLayoutManager(this, 3));
        mCashMoneyListView.setAdapter(cashMoneyAdapter);

        cashMoneyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == lastIndex) {
                    return;
                }
                cashMoneyAdapter.getData().get(position).setSelected(true);
                cashMoneyAdapter.getData().get(lastIndex).setSelected(false);
                lastIndex = position;
                cashMoneyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_cash_record)
    void cashRecord() {
        Intent intent = new Intent(this, CashRecordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.cash_gif)
    void cashNow() {
        Intent intent = new Intent(this, BindPhoneActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_go_to_bind)
    void gotoBind() {
        if (loginOutDialog != null && !loginOutDialog.isShowing()) {
            loginOutDialog.show();
            loginOutDialog.setDialogInfo("绑定手机提醒", "为了你的账号安全，请先绑定手机号");
        }
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

}
