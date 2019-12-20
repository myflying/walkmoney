package com.ydys.elsbballs.ui.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.elsbballs.R;
import com.ydys.elsbballs.bean.QuestionInfo;

import java.util.List;


public class QuestionAdapter extends BaseQuickAdapter<QuestionInfo, BaseViewHolder> {

    private Context mContext;

    public QuestionAdapter(Context context, List<QuestionInfo> datas) {
        super(R.layout.question_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, QuestionInfo temp) {
        holder.setText(R.id.tv_question_title, temp.getTitle())
                .setText(R.id.tv_question_content, temp.getContent());
        holder.addOnClickListener(R.id.tv_question_title);
        holder.setGone(R.id.tv_question_content, temp.isShow() ? true : false);
        holder.setGone(R.id.line_view, temp.isShow() ? false : true);
    }
}