package com.ydys.elsbballs.ui.adapter;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.elsbballs.R;
import com.ydys.elsbballs.bean.CashRecordInfo;
import com.ydys.elsbballs.util.MatrixUtils;

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