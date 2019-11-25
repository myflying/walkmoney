package com.ydys.moneywalk.ui.fragment;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.ui.adapter.SignDayAdapter;
import com.ydys.moneywalk.ui.adapter.TaskInfoAdapter;
import com.ydys.moneywalk.ui.custom.NormalDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MakeMoneyFragment extends BaseFragment {

    @BindView(R.id.sign_list_view)
    RecyclerView mSignDayListView;

    @BindView(R.id.task_list_view)
    RecyclerView mTaskInfoListView;

    SignDayAdapter signDayAdapter;

    TaskInfoAdapter taskInfoAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_make_money;
    }

    @Override
    public void initVars() {

    }

    @Override
    public void initViews() {
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
    }

    @Override
    public void loadData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(i + "");
        }
        signDayAdapter = new SignDayAdapter(getActivity(), list);
        mSignDayListView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        mSignDayListView.setAdapter(signDayAdapter);

        taskInfoAdapter = new TaskInfoAdapter(getActivity(), list);
        mTaskInfoListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskInfoListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(getActivity(),R.color.line1_color),2));
        mTaskInfoListView.setAdapter(taskInfoAdapter);
    }
}
