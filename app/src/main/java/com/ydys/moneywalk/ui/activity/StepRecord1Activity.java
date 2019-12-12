package com.ydys.moneywalk.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.custom.step.BindService;
import com.ydys.moneywalk.ui.custom.step.UpdateUiCallBack;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;

public class StepRecord1Activity extends BaseActivity {

    @BindView(R.id.tv_step)
    TextView textView;

    private BindService bindService;

    private boolean isBind;

    /**
     * 当天的时间"yyyy-MM-dd"
     */
    String todayDate;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                textView.setText(msg.arg1 + "");
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_step_record1;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        todayDate = TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));

        Intent intent = new Intent(StepRecord1Activity.this, BindService.class);
        isBind = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    //和绷定服务数据交换的桥梁，可以通过IBinder service获取服务的实例来调用服务的方法或者数据
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.i("onServiceConnected--->");
            textView.setText(SPUtils.getInstance().getInt(todayDate, 0) + "");

            BindService.LcBinder lcBinder = (BindService.LcBinder) service;
            bindService = lcBinder.getService();
            bindService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    //当前接收到stepCount数据，就是最新的步数
                    Message message = Message.obtain();
                    message.what = 1;
                    message.arg1 = stepCount;
                    handler.sendMessage(message);
                    Logger.i("current step --->" + stepCount);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onDestroy() {  //app被关闭之前，service先解除绑定
        super.onDestroy();
        if (isBind) {
            this.unbindService(serviceConnection);
        }
    }
}
