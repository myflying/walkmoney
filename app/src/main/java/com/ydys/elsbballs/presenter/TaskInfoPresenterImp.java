package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.base.BasePresenterImp;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.TaskInfoWrapperRet;
import com.ydys.elsbballs.model.TaskInfoModelImp;

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
