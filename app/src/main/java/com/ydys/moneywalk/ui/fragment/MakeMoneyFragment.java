package com.ydys.moneywalk.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.SigningInfo;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import com.ydys.moneywalk.bean.SignInfoRet;
import com.ydys.moneywalk.bean.TakeGoldInfo;
import com.ydys.moneywalk.bean.TakeGoldInfoRet;
import com.ydys.moneywalk.bean.TaskInfo;
import com.ydys.moneywalk.bean.TaskInfoWrapper;
import com.ydys.moneywalk.bean.TaskInfoWrapperRet;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.SignInfoPresenterImp;
import com.ydys.moneywalk.presenter.TakeGoldInfoPresenterImp;
import com.ydys.moneywalk.presenter.TaskInfoPresenterImp;
import com.ydys.moneywalk.presenter.UserInfoPresenterImp;
import com.ydys.moneywalk.ui.activity.BindPhoneActivity;
import com.ydys.moneywalk.ui.activity.BodyDataActivity;
import com.ydys.moneywalk.ui.activity.CashActivity;
import com.ydys.moneywalk.ui.activity.FillInCodeActivity;
import com.ydys.moneywalk.ui.activity.InviteFriendActivity;
import com.ydys.moneywalk.ui.activity.MakeMoneyActivity;
import com.ydys.moneywalk.ui.adapter.SignDayAdapter;
import com.ydys.moneywalk.ui.adapter.TaskInfoAdapter;
import com.ydys.moneywalk.ui.custom.NormalDecoration;
import com.ydys.moneywalk.ui.custom.ReceiveGoldDialog;
import com.ydys.moneywalk.ui.custom.SignTodayDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class MakeMoneyFragment extends BaseFragment implements IBaseView, ReceiveGoldDialog.GoldDialogListener, SignTodayDialog.SignDayListener {

    public static int BIND_PHONE_CODE = 1000;

    public static int FILL_IN_CODE = 1001;

    public static int BODY_DATA_CODE = 1002;

    public static int CASH_CODE = 1003;

    public static int INVITE_CODE = 1004;

    public static int COUNT_SPACE = 1 * 60;

    @BindView(R.id.sign_list_view)
    RecyclerView mSignDayListView;

    @BindView(R.id.task_list_view)
    RecyclerView mTaskInfoListView;

    @BindView(R.id.tv_total_gold_num)
    TextView mTotalGoldNumTv;

    @BindView(R.id.tv_money)
    TextView mMoneyTv;

    @BindView(R.id.tv_sign_days)
    TextView mSignDaysTv;

    @BindView(R.id.tv_tomorrow_gold)
    TextView mTomorrowGoldTv;

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

    SignInfoPresenterImp signInfoPresenterImp;

    SignTodayDialog signTodayDialog;

    private int currentDoubleDayIndex;

    DecimalFormat df = new DecimalFormat("0.00");

    TaskInfoWrapper.SignTaskInfo signTaskInfo;

    private String taskType;

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
        receiveGoldDialog = new ReceiveGoldDialog(getActivity(), R.style.gold_dialog);
        receiveGoldDialog.setGoldDialogListener(this);
    }

    @Override
    public void loadData() {
        taskInfoPresenterImp = new TaskInfoPresenterImp(this, getActivity());
        userInfoPresenterImp = new UserInfoPresenterImp(this, getActivity());
        takeGoldInfoPresenterImp = new TakeGoldInfoPresenterImp(this, getActivity());
        signInfoPresenterImp = new SignInfoPresenterImp(this, getActivity());

        signDayAdapter = new SignDayAdapter(getActivity(), null);
        mSignDayListView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        mSignDayListView.setAdapter(signDayAdapter);

        signDayAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.iv_double_icon) {
                    currentDoubleDayIndex = position;
                    Logger.i("点击了翻倍--->" + position);
                    todayDouble(signDayAdapter.getData().get(position).getDay());
                }
            }
        });

        taskInfoAdapter = new TaskInfoAdapter(getActivity(), null);
        mTaskInfoListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mTaskInfoListView.addItemDecoration(new NormalDecoration(ContextCompat.getColor(getActivity(), R.color.line1_color), 2));
        mTaskInfoListView.setAdapter(taskInfoAdapter);

        taskInfoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                currentTaskIndex = position;
                TaskInfo taskInfo = taskInfoAdapter.getData().get(position);
                if (view.getId() == R.id.btn_get_now && taskInfo.getState() < 3) {
                    if (taskInfo.getTaskType().equals("ad")) {
                        taskType = "ad";
                        taskGetGold(taskInfo);
                    }

                    if (taskInfo.getTaskType().equals("invite")) {
                        taskType = "invite";
                        if (taskInfo.getState() == 1) {
                            Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
                            intent.putExtra("share_type", 1);
                            startActivityForResult(intent, INVITE_CODE);
                        }
                        if (taskInfo.getState() == 2) {
                            taskGetGold(taskInfo);
                        }
                    }

                    if (taskInfo.getTaskType().equals("bind_sj")) {
                        taskType = "bind_sj";
                        if (taskInfo.getState() == 1) {
                            Intent intent = new Intent(getActivity(), BindPhoneActivity.class);
                            startActivityForResult(intent, BIND_PHONE_CODE);
                        }
                        if (taskInfo.getState() == 2) {
                            taskGetGold(taskInfo);
                        }
                    }
                    if (taskInfo.getTaskType().equals("share")) {
                        taskType = "share";
                        if (taskInfo.getState() == 1) {
                            Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
                            intent.putExtra("share_type", 2);
                            startActivity(intent);
                        }
                        if (taskInfo.getState() == 2) {
                            taskGetGold(taskInfo);
                        }
                    }
                    if (taskInfo.getTaskType().equals("bind_wx")) {
                        taskType = "bind_wx";
                        if (taskInfo.getState() == 1) {
                            mShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, authListener);
                        }
                        if (taskInfo.getState() == 2) {
                            taskGetGold(taskInfo);
                        }
                    }
                    if (taskInfo.getTaskType().equals("invite_code")) {
                        taskType = "invite_code";
                        if (taskInfo.getState() == 1) {
                            Intent intent = new Intent(getActivity(), FillInCodeActivity.class);
                            startActivityForResult(intent, FILL_IN_CODE);
                        }
                        if (taskInfo.getState() == 2) {
                            taskGetGold(taskInfo);
                        }
                    }
                    if (taskInfo.getTaskType().equals("target_num")) {
                        taskType = "target_num";
                        if (taskInfo.getState() == 1) {
                            Intent intent = new Intent(getActivity(), BodyDataActivity.class);
                            startActivityForResult(intent, BODY_DATA_CODE);
                        }
                        if (taskInfo.getState() == 2) {
                            taskGetGold(taskInfo);
                        }
                    }
                    if (taskInfo.getTaskType().equals("first_cash")) {
                        taskType = "first_cash";
                        if (taskInfo.getState() == 1) {
                            Intent intent = new Intent(getActivity(), CashActivity.class);
                            startActivityForResult(intent, CASH_CODE);
                        }
                        if (taskInfo.getState() == 2) {
                            taskGetGold(taskInfo);
                        }
                    }
                    if (taskInfo.getTaskType().equals("body_data")) {
                        taskType = "body_data";
                        if (taskInfo.getState() == 1) {
                            Intent intent = new Intent(getActivity(), BodyDataActivity.class);
                            startActivityForResult(intent, BODY_DATA_CODE);
                        }
                        if (taskInfo.getState() == 2) {
                            taskGetGold(taskInfo);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("make fragment --->" + requestCode + "---->" + resultCode);
        taskInfoPresenterImp.taskList(App.mUserInfo != null ? App.mUserInfo.getId() : "");
    }

    /**
     * 每个任务状态为“立即领取”时，开始领钱金币
     *
     * @param taskInfo
     */
    void taskGetGold(TaskInfo taskInfo) {
        TakeGoldInfo takeGoldInfo = new TakeGoldInfo();
        takeGoldInfo.setUserId(App.mUserInfo != null ? App.mUserInfo.getId() : "");
        takeGoldInfo.setTaskId(taskInfo.getTaskId());
        takeGoldInfo.setGold(taskInfo.getGold());
        takeGoldInfoPresenterImp.takeTaskGold(takeGoldInfo);
    }

    public void makeMoneySelect() {
        taskType = "";
        userInfoPresenterImp.imeiLogin(PhoneUtils.getIMEI(), "10000", "yangcheng");
        taskInfoPresenterImp.taskList(App.mUserInfo != null ? App.mUserInfo.getId() : "");
    }

    /**
     * 当日金币翻倍
     */
    public void todayDouble(String day) {
        taskType = "sign_today";
        signInfoPresenterImp.signDay(App.mUserInfo != null ? App.mUserInfo.getId() : "", day);
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
                    signTaskInfo = ((TaskInfoWrapperRet) tData).getData().getSignTaskInfo();
                    mSignDaysTv.setText(signTaskInfo.getContinueNum() + "天");
                    mTomorrowGoldTv.setText(signTaskInfo.getTomorrowGold() + "");

                    taskInfoAdapter.setNewData(((TaskInfoWrapperRet) tData).getData().getTaskList());
                    signDayAdapter.setSignDay(signTaskInfo.getContinueNum());
                    signDayAdapter.setNewData(signTaskInfo.getList());

                    if (signTaskInfo.getShowSignSuss() == 1) {
                        signTodayDialog = new SignTodayDialog(getActivity(), R.style.common_dialog);
                        signTodayDialog.setSignDayListener(this);
                        signTodayDialog.show();

                        int index = signTaskInfo.getContinueNum() - 1;
                        int currentGold = signTaskInfo.getList().get(index).getGold();
                        double money = 0;
                        if (App.initInfo != null && App.initInfo.getAppConfig() != null) {
                            money = (double) signTaskInfo.getGold() / (double) App.initInfo.getAppConfig().getExchangeRate();
                        }

                        signTodayDialog.updateTodayInfo(signTaskInfo.getContinueNum() + "", currentGold + "", signTaskInfo.getGold() + "", df.format(money));
                    }
                }
            }

            if (tData instanceof UserInfoRet) {
                if (((UserInfoRet) tData).getCode() == Constants.SUCCESS) {
                    SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(((UserInfoRet) tData).getData()));
                    App.mUserInfo = ((UserInfoRet) tData).getData();
                    App.isLogin = true;
                    mTotalGoldNumTv.setText(App.mUserInfo.getGold() + "");
                    mMoneyTv.setText("≈" + App.mUserInfo.getAmount() + "元");

                    if (taskType.equals("bind_sj") || taskType.equals("bind_wx")) {
                        SPUtils.getInstance().put(Constants.LOCAL_LOGIN, true);
                        Toasty.normal(getActivity(), "绑定成功").show();
                        taskInfoPresenterImp.taskList(App.mUserInfo != null ? App.mUserInfo.getId() : "");
                    }
                } else {
                    Toasty.normal(getActivity(), ((UserInfoRet) tData).getMsg()).show();
                }
            }

            if (tData instanceof TakeGoldInfoRet) {
                if (((TakeGoldInfoRet) tData).getCode() == Constants.SUCCESS) {
                    TakeGoldInfo takeGoldInfo = ((TakeGoldInfoRet) tData).getData();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showLong("领取成功");
                            if (receiveGoldDialog == null) {
                                receiveGoldDialog = new ReceiveGoldDialog(getActivity(), R.style.gold_dialog);
                                receiveGoldDialog.setGoldDialogListener(MakeMoneyFragment.this);
                            }

                            receiveGoldDialog.show();
                            int currentGold = 0;
                            if (signTaskInfo != null) {
                                int index = signTaskInfo.getContinueNum() - 1;
                                currentGold = signDayAdapter.getData().get(index).getGold();
                            }
                            receiveGoldDialog.updateGoldInfo(currentGold + "", takeGoldInfo.getGold() + "", "≈" + takeGoldInfo.getAmount() + "元");
                        }
                    }, 2000);
                } else {
                    Toasty.normal(getActivity(), ((TakeGoldInfoRet) tData).getMsg()).show();
                }
            }

            if (tData instanceof SignInfoRet) {
                if (((SignInfoRet) tData).getCode() == Constants.SUCCESS) {
                    if (signDayAdapter != null) {
                        signDayAdapter.getData().get(currentDoubleDayIndex).setIsDouble(1);
                        signDayAdapter.notifyDataSetChanged();
                    }
                    showGoldDialog(((SignInfoRet) tData).getData().getGold());
                } else {
                    ToastUtils.showLong(((SignInfoRet) tData).getMsg());
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

    //弹出签到/翻倍领取成功
    public void showGoldDialog(int totalGold) {
        if (receiveGoldDialog == null) {
            receiveGoldDialog = new ReceiveGoldDialog(getActivity(), R.style.gold_dialog);
            receiveGoldDialog.setGoldDialogListener(MakeMoneyFragment.this);
        }
        receiveGoldDialog.show();
        int currentGold = 0;
        if (signTaskInfo != null) {
            int index = signTaskInfo.getContinueNum() - 1;
            currentGold = signDayAdapter.getData().get(index).getGold();
        }
        double money = 0;
        if (App.initInfo != null && App.initInfo.getAppConfig() != null) {
            money = (double) totalGold / (double) App.initInfo.getAppConfig().getExchangeRate();
        }
        receiveGoldDialog.updateGoldInfo(currentGold + "", totalGold + "", "≈" + money + "元");
    }

    @Override
    public void closeGoldDialog() {
        if (receiveGoldDialog != null && receiveGoldDialog.isShowing()) {
            receiveGoldDialog.dismiss();
            receiveGoldDialog = null;
        }
        if (taskType.equals("ad")) {
            long tempDate = TimeUtils.getNowMills();
            SPUtils.getInstance().put("see_video_date", tempDate);
        }
        //翻倍完成时，弹出关闭窗口，不刷新任务列表
        if (!taskType.equals("sign_today")) {
            taskInfoPresenterImp.taskList(App.mUserInfo != null ? App.mUserInfo.getId() : "");
        }
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

                    userInfoPresenterImp.login(PhoneUtils.getIMEI(), "wechat", data.get("openid"), "", data.get("name"), data.get("iconurl"));
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

    @Override
    public void doubleGoldVideo() {
        ToastUtils.showLong("模拟观看视频完成，金币翻倍");
        if (signTaskInfo != null) {
            int index = signTaskInfo.getContinueNum() - 1;
            String day = signDayAdapter.getData().get(index).getDay();
            todayDouble(day);
        }
    }
}
