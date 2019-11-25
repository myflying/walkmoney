package com.ydys.moneywalk.ui.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.CashMoneyInfo;
import com.ydys.moneywalk.bean.GoldDetailInfo;

import java.util.List;


public class GoldDetailAdapter extends BaseQuickAdapter<GoldDetailInfo, BaseViewHolder> {

    private Context mContext;

    public GoldDetailAdapter(Context context, List<GoldDetailInfo> datas) {
        super(R.layout.gold_detail_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, GoldDetailInfo temp) {

    }
}