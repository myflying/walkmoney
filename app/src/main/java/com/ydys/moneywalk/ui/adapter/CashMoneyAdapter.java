package com.ydys.moneywalk.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.CashMoneyInfo;

import java.util.List;


public class CashMoneyAdapter extends BaseQuickAdapter<CashMoneyInfo, BaseViewHolder> {

    private Context mContext;

    public CashMoneyAdapter(Context context, List<CashMoneyInfo> datas) {
        super(R.layout.cash_money_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CashMoneyInfo temp) {
        holder.setBackgroundRes(R.id.layout_cash_money, temp.isSelected() ? R.mipmap.cash_money_selected : R.mipmap.cash_money_normal);
    }
}