package com.ydys.elsbballs.presenter;

import android.content.Context;

import com.ydys.elsbballs.model.executer.JobExecutor;
import com.ydys.elsbballs.model.executer.RxJavaExecuter;
import com.ydys.elsbballs.model.executer.UIThread;


/**
 * Created by Administrator on 2017/2/17.
 */

public abstract class Presenter {

    protected Context context;
    protected RxJavaExecuter rxJavaExecuter;

    public Presenter(Context context){
        this.context = context;
        this.rxJavaExecuter = new RxJavaExecuter(JobExecutor.instance(), UIThread.instance());
    }

    public abstract void destroy();
}
