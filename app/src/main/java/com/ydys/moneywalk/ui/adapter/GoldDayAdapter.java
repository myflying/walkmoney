package com.ydys.moneywalk.ui.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.CashMoneyInfo;
import com.ydys.moneywalk.bean.GoldDayInfo;

import java.util.List;


public class GoldDayAdapter extends BaseQuickAdapter<GoldDayInfo, BaseViewHolder> {

    private Context mContext;

    public GoldDayAdapter(Context context, List<GoldDayInfo> datas) {
        super(R.layout.gold_day_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, GoldDayInfo temp) {
        RecyclerView detailListView = holder.getView(R.id.gold_detail_list_view);
        GoldDetailAdapter goldDetailAdapter = new GoldDetailAdapter(mContext, temp.getDetailList());
        detailListView.setLayoutManager(new LinearLayoutManager(mContext));
        detailListView.setAdapter(goldDetailAdapter);
    }
}