package com.ydys.moneywalk.presenter;

import android.content.Context;

import com.ydys.moneywalk.base.BasePresenterImp;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.TaskInfoWrapperRet;
import com.ydys.moneywalk.model.TaskInfoModelImp;

/**
 * Created by admin on 2017/4/7.
 */

public class TaskInfoPresenterImp extends BasePresenterImp<IBaseView, TaskInfoWrapperRet> implements TaskInfoPresenter {

    private Context context = null;
    private TaskInfoModelImp taskInfoModelImp = null;

    public TaskInfoPresenterImp(IBaseView view, Context context) {
        super(view);
        this.context = context;
        this.taskInfoModelImp = new TaskInfoModelImp(context);
    }

    @Override
    public void taskList(String userId, int isLogin) {
        taskInfoModelImp.taskList(userId, isLogin, this);
    }
}
