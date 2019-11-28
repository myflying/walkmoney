package com.ydys.moneywalk.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.TakeGoldInfo;
import com.ydys.moneywalk.bean.TakeGoldInfoRet;
import com.ydys.moneywalk.bean.TaskInfo;
import com.ydys.moneywalk.bean.TaskInfoWrapperRet;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.TakeGoldInfoPresenterImp;
import com.ydys.moneywalk.presenter.TaskInfoPresenterImp;
import com.ydys.moneywalk.presenter.UserInfoPresenterImp;
import com.ydys.moneywalk.ui.activity.BindPhoneActivity;
import com.ydys.moneywalk.ui.activity.InviteFriendActivity;
import com.ydys.moneywalk.ui.activity.MakeMoneyActivity;
import com.ydys.moneywalk.ui.adapter.SignDayAdapter;
import com.ydys.moneywalk.ui.adapter.TaskInfoAdapter;
import com.ydys.moneywalk.ui.custom.NormalDecoration;
import com.ydys.moneywalk.ui.custom.ReceiveGoldDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class MakeMoneyFragment extends BaseFragment implements IBaseView, ReceiveGoldDialog.GoldDialogListener {

    public static int COUNT_SPACE = 1 * 60;

    @BindView(R.id.sign_list_view)
    RecyclerView mSignDayListView;

    @BindView(R.id.task_list_view)
    RecyclerView mTaskInfoListView;

    SignDayAdapter signDayAdapter;

    TaskInfoAdapter taskInfoAdapter;

    TaskInfoPresenterImp taskInfoPresenterImp;

    ReceiveGoldDialog receiveGoldDialog;

    private Handler mHandler = new Handler();

    private int currentTaskIndex;

    private UMShareAPI mShareAPI = null;

    private ProgressDialog progressDialog = null;

    UserInfoPresenterImp userInfoPresenterImp;

    TakeGoldInfoPresenterImp takeGoldInfoPresenterImp;

    @Override
    protected int getContentView() {
        return R.layout.fragment_make_money;
    }

    @Override
    public void initVars() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在登录");

        mShareAPI = UMShareAPI.get(getActivity());
    }

    @Override
    public void initViews() {
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
        receiveGoldDialog = new ReceiveGoldDialog(getActivity(), R.style.common_dialog);
        receiveGoldDialog.setGoldDialogListener(this);
    }

    @Override
    public void loadData() {
        taskInfoPresenterImp = new TaskInfoPresenterImp(this, getActivity());
        taskInfoPresenterImp.taskList(App.mUserInfo != null ? App.mUserInfo.getId() : "");
        userInfoPresenterImp = new UserInfoPresenterImp(this, getActivity());
        takeGoldInfoPresenterImp = new TakeGoldInfoPresenterImp(this, getActivity());

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(i + "");
        }
        signDayAdapter = new SignDayAdapter(getActivity(), list);
        mSignDayListView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        mSignDayListView.setAdapter(signDayAdapter);

        taskInfoAdapter = new TaskInfoAdapter(getActivity(), null);
        mTaskInfoListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskInfoListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(getActivity(), R.color.line1_color), 2));
        mTaskInfoListView.setAdapter(taskInfoAdapter);

        taskInfoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                currentTaskIndex = position;
                TaskInfo taskInfo = taskInfoAdapter.getData().get(position);
                if (view.getId() == R.id.btn_get_now) {
                    if (taskInfo.getTaskType().equals("ad")) {

                        TakeGoldInfo takeGoldInfo = new TakeGoldInfo();
                        takeGoldInfo.setUserId(App.mUserInfo != null ? App.mUserInfo.getId() : "");
                        takeGoldInfo.setTaskId(taskInfo.getTaskId());
                        takeGoldInfo.setGold(taskInfo.getGold() + "");
                        takeGoldInfoPresenterImp.takeTaskGold(takeGoldInfo);
                    }

                    if (taskInfo.getTaskType().equals("invite")) {
                        Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
                        startActivity(intent);
                    }

                    if (taskInfo.getTaskType().equals("bind_sj")) {
                        if (taskInfo.getComplete() == 0) {
                            Intent intent = new Intent(getActivity(), BindPhoneActivity.class);
                            startActivity(intent);
                        }
                    }
                    if (taskInfo.getTaskType().equals("share")) {
                        Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
                        startActivity(intent);
                    }
                    if (taskInfo.getTaskType().equals("bind_wx")) {
                        mShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, authListener);
                    }

                }
            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(Object tData) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Logger.i(JSON.toJSONString(tData));

        if (tData != null) {
            if (tData instanceof TaskInfoWrapperRet && ((TaskInfoWrapperRet) tData).getCode() == Constants.SUCCESS) {
                if (((TaskInfoWrapperRet) tData).getData() != null) {
                    taskInfoAdapter.setNewData(((TaskInfoWrapperRet) tData).getData().getTaskList());
                }
            }

            if (tData instanceof UserInfoRet) {
                if (((UserInfoRet) tData).getCode() == Constants.SUCCESS) {
                    Toasty.normal(getActivity(), "绑定成功").show();
                } else {
                    Toasty.normal(getActivity(), ((UserInfoRet) tData).getMsg()).show();
                }
            }

            if (tData instanceof TakeGoldInfoRet) {
                if (((TakeGoldInfoRet) tData).getCode() == Constants.SUCCESS) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showLong("领取成功");
                            if (receiveGoldDialog == null) {
                                receiveGoldDialog = new ReceiveGoldDialog(getActivity(), R.style.common_dialog);
                                receiveGoldDialog.setGoldDialogListener(MakeMoneyFragment.this);
                            }

                            receiveGoldDialog.show();
                        }
                    }, 2000);
                } else {
                    Toasty.normal(getActivity(), ((TakeGoldInfoRet) tData).getMsg()).show();
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void closeGoldDialog() {
        if (receiveGoldDialog != null && receiveGoldDialog.isShowing()) {
            receiveGoldDialog.dismiss();
            receiveGoldDialog = null;
        }

        long tempDate = TimeUtils.getNowMills();
        SPUtils.getInstance().put("see_video_date", tempDate);
        taskInfoAdapter.notifyItemChanged(currentTaskIndex);
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Logger.i("auth data --->" + JSONObject.toJSONString(data));
            //Toast.makeText(mContext, "授权成功了", Toast.LENGTH_LONG).show();
            try {
                App.isLogin = true;
                if (data != null) {
                    if (progressDialog != null && !progressDialog.isShowing()) {
                        progressDialog.show();
                    }

                    userInfoPresenterImp.login(PhoneUtils.getIMEI(), "wechat", data.get("uid"), "", data.get("name"), data.get("iconurl"));
                    Logger.i("wx login info--->" + data.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            //Toast.makeText(mContext, "授权失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
            Toasty.normal(getActivity(), "授权失败").show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toasty.normal(getActivity(), "授权取消").show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    };

    @OnClick(R.id.layout_act_rule)
    void makeMoney() {
        Intent intent = new Intent(getActivity(), MakeMoneyActivity.class);
        startActivity(intent);
    }
}
