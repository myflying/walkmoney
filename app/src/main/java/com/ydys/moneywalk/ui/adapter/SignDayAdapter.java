package com.ydys.moneywalk.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.SignInfo;

import java.util.List;


public class SignDayAdapter extends BaseQuickAdapter<SignInfo, BaseViewHolder> {

    private Context mContext;

    private int signDay;

    public void setSignDay(int day) {
        this.signDay = day;
    }

    public SignDayAdapter(Context context, List<SignInfo> datas) {
        super(R.layout.sign_day_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, SignInfo temp) {
        if (holder.getAdapterPosition() > signDay - 1) {
            holder.setBackgroundRes(R.id.tv_day_gold, R.mipmap.sign_day_gold_bg);
            holder.setVisible(R.id.tv_day_gold, true);
            holder.setVisible(R.id.iv_double_icon, false);
        } else {
            holder.setBackgroundRes(R.id.tv_day_gold, R.mipmap.sign_day_normal);
            holder.setVisible(R.id.tv_day_gold, false);
            holder.setVisible(R.id.iv_double_icon, true);
        }
    }
}