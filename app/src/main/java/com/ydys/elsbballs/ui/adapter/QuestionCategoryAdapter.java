package com.ydys.elsbballs.ui.adapter;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ydys.elsbballs.R;
import com.ydys.elsbballs.bean.QuestionCategory;

import java.util.List;


public class QuestionCategoryAdapter extends BaseQuickAdapter<QuestionCategory, BaseViewHolder> {

    private Context mContext;

    public QuestionCategoryAdapter(Context context, List<QuestionCategory> datas) {
        super(R.layout.question_category_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, QuestionCategory temp) {
        holder.setText(R.id.tv_question_category, temp.getName());

        RecyclerView questionListView = holder.getView(R.id.question_list_view);
        QuestionAdapter questionAdapter = new QuestionAdapter(mContext, temp.getQuestionInfoList());
        questionListView.setLayoutManager(new LinearLayoutManager(mContext));
        questionListView.setAdapter(questionAdapter);
        questionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_question_title) {
                    boolean isShow = !questionAdapter.getData().get(position).isShow();
                    questionAdapter.getData().get(position).setShow(isShow);
                }
                questionAdapter.notifyDataSetChanged();
            }
        });
    }
}