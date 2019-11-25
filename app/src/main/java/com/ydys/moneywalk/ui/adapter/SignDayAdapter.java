package com.ydys.moneywalk.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.moneywalk.R;

import java.util.List;


public class SignDayAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public SignDayAdapter(Context context, List<String> datas) {
        super(R.layout.sign_day_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, String temp) {
        //if (holder.getAdapterPosition() == 0) {
        ImageView mDoubleIv = holder.getView(R.id.iv_double_icon);
//        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.double_item);
//        animation.setDuration(2000);
//        animation.setRepeatCount(-1);
//        mDoubleIv.startAnimation(animation);
        //}
    }
}