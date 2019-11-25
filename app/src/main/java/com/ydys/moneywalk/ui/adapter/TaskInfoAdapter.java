package com.ydys.moneywalk.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.moneywalk.R;

import java.util.List;


public class TaskInfoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public TaskInfoAdapter(Context context, List<String> datas) {
        super(R.layout.task_info_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, String temp) {

    }
}