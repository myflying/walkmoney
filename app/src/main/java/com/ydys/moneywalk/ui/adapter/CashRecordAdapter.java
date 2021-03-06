package com.ydys.moneywalk.ui.adapter;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.CashMoneyInfo;
import com.ydys.moneywalk.bean.CashRecordInfo;
import com.ydys.moneywalk.util.MatrixUtils;

import java.text.DecimalFormat;
import java.util.List;


public class CashRecordAdapter extends BaseQuickAdapter<CashRecordInfo, BaseViewHolder> {

    private Context mContext;

    public CashRecordAdapter(Context context, List<CashRecordInfo> datas) {
        super(R.layout.cash_record_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CashRecordInfo temp) {
        holder.setText(R.id.tv_cash_date, temp.getAddTime())
                .setText(R.id.tv_cash_money, "+" + MatrixUtils.getPrecisionMoney(temp.getMoney()))
                .setText(R.id.tv_cash_state, temp.getStatus() == 1 ? "已到账" : "提现中");
        holder.setTextColor(R.id.tv_cash_state, ContextCompat.getColor(mContext, temp.getStatus() == 1 ? R.color.wx_login_color : R.color.gold_num_color));
    }
}