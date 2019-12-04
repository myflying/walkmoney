package com.ydys.moneywalk.ui.adapter;

import android.content.Context;

import androidx.core.content.ContextCompat;
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
        holder.setText(R.id.tv_gold_date, temp.getTime())
                .setText(R.id.tv_step_title, temp.getTitle())
                .setText(R.id.tv_gold, temp.getType() == 1 ? "+" + temp.getNum() : "-" + temp.getNum());
        //holder.setTextColor(R.id.tv_gold, ContextCompat.getColor(mContext, temp.getType() == 1 ? R.color.privacy_text_color : R.color.wx_login_color));
    }
}