package com.ydys.moneywalk.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.CashMoneyItem;

import java.util.List;


public class CashMoneyAdapter extends BaseQuickAdapter<CashMoneyItem, BaseViewHolder> {

    private Context mContext;

    public CashMoneyAdapter(Context context, List<CashMoneyItem> datas) {
        super(R.layout.cash_money_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, CashMoneyItem temp) {

        if (temp.getIsNewPeople() == 1) {
            holder.setBackgroundRes(R.id.layout_cash_money, temp.isSelected() ? R.mipmap.new_per_selected : R.mipmap.new_per_normal);
        } else {
            holder.setBackgroundRes(R.id.layout_cash_money, temp.isSelected() ? R.mipmap.cash_money_selected : R.mipmap.cash_money_normal);
        }

        holder.setText(R.id.tv_cash_money, temp.getAmount() + "元")
                .setText(R.id.tv_cash_gold, "售价" + temp.getNeedGold() + "金币");
    }
}