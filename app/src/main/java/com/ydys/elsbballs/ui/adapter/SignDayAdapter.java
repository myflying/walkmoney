package com.ydys.elsbballs.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.elsbballs.R;
import com.ydys.elsbballs.bean.SignInfo;

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
        ImageView mDoubleIv = holder.getView(R.id.iv_double_icon);
        //未签到
        if (holder.getAdapterPosition() > signDay - 1) {
            if (holder.getAdapterPosition() == 6) {
                holder.setBackgroundRes(R.id.tv_day_gold, R.mipmap.cheat_icon);
                holder.setVisible(R.id.layout_top_bubble, true);
                holder.setBackgroundRes(R.id.layout_top_bubble, R.mipmap.add_gold_bubble);
                holder.setText(R.id.tv_top_gold, "神秘宝箱");
            } else if (holder.getAdapterPosition() == signDay) {
                holder.setBackgroundRes(R.id.tv_day_gold, R.mipmap.sign_day_gold_bg);
                holder.setVisible(R.id.layout_top_bubble, true);
                holder.setText(R.id.tv_top_gold, "翻2倍");
                holder.setBackgroundRes(R.id.layout_top_bubble, R.mipmap.top_double_bg);
            } else {
                holder.setBackgroundRes(R.id.tv_day_gold, R.mipmap.sign_day_gold_bg);
                holder.setBackgroundRes(R.id.layout_top_bubble, R.mipmap.add_gold_bubble);
                holder.setVisible(R.id.layout_top_bubble, false);
            }

            holder.setVisible(R.id.tv_day_gold, true);
            holder.setVisible(R.id.iv_double_icon, false);
            holder.addOnClickListener(R.id.tv_day_gold);
            mDoubleIv.setClickable(false);
            holder.setText(R.id.tv_sign_day_num, (holder.getAdapterPosition() + 1) + "天");
        } else {
            //已翻倍
            if (temp.getIsDouble() == 1) {
                holder.setBackgroundRes(R.id.tv_day_gold, R.mipmap.sign_day_check_in);
                holder.setVisible(R.id.tv_day_gold, true);
                holder.setVisible(R.id.iv_double_icon, false);
                mDoubleIv.setClickable(false);
                holder.setVisible(R.id.layout_top_bubble, false);
                holder.setText(R.id.tv_sign_day_num, "已领");
            } else {

                if (holder.getAdapterPosition() < signDay - 1) {
                    //未翻倍
                    holder.setBackgroundRes(R.id.tv_day_gold, R.mipmap.sign_day_check_in);
                    holder.setVisible(R.id.tv_day_gold, true);
                    holder.setVisible(R.id.iv_double_icon, false);
                    mDoubleIv.setClickable(false);
                    holder.setVisible(R.id.layout_top_bubble, false);
                    holder.setText(R.id.tv_sign_day_num, "已领");
                } else {
                    //未翻倍
                    holder.setBackgroundRes(R.id.tv_day_gold, R.mipmap.sign_day_gold_bg);
                    holder.setVisible(R.id.tv_day_gold, false);
                    holder.setVisible(R.id.iv_double_icon, true);
                    mDoubleIv.setClickable(true);
                    holder.setVisible(R.id.layout_top_bubble, true);
                    holder.setText(R.id.tv_top_gold, "+" + temp.getGold() + "金币");
                    holder.setText(R.id.tv_sign_day_num, "可翻倍");
                }
            }
            //holder.setText(R.id.tv_sign_day_num, "已领");
        }
        holder.setText(R.id.tv_day_gold, holder.getAdapterPosition() == 6 ? "" : temp.getGold() + "");
        holder.addOnClickListener(R.id.iv_double_icon);
    }
}