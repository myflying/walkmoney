package com.ydys.elsbballs.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ydys.elsbballs.App;
import com.ydys.elsbballs.R;
import com.ydys.elsbballs.base.IBaseView;
import com.ydys.elsbballs.bean.SignInfoRet;
import com.ydys.elsbballs.bean.TakeGoldInfo;
import com.ydys.elsbballs.bean.TakeGoldInfoRet;
import com.ydys.elsbballs.bean.TaskInfo;
import com.ydys.elsbballs.bean.TaskInfoWrapper;
import com.ydys.elsbballs.bean.TaskInfoWrapperRet;
import com.ydys.elsbballs.bean.UserInfoRet;
import com.ydys.elsbballs.common.Constants;
import com.ydys.elsbballs.presenter.SignInfoPresenterImp;
import com.ydys.elsbballs.presenter.TakeGoldInfoPresenterImp;
import com.ydys.elsbballs.presenter.TaskInfoPresenterImp;
import com.ydys.elsbballs.presenter.UserInfoPresenterImp;
import com.ydys.elsbballs.ui.activity.BindPhoneActivity;
import com.ydys.elsbballs.ui.activity.BodyDataActivity;
import com.ydys.elsbballs.ui.activity.CashActivity;
import com.ydys.elsbballs.ui.activity.FillInCodeActivity;
import com.ydys.elsbballs.ui.activity.InviteFriendActivity;
import com.ydys.elsbballs.ui.activity.MakeMoneyActivity;
import com.ydys.elsbballs.ui.activity.PhoneLoginActivity;
import com.ydys.elsbballs.ui.adapter.SignDayAdapter;
import com.ydys.elsbballs.ui.adapter.TaskInfoAdapter;
import com.ydys.elsbballs.ui.custom.DislikeDialog;
import com.ydys.elsbballs.ui.custom.LoginDialog;
import com.ydys.elsbballs.ui.custom.NormalDecoration;
import com.ydys.elsbballs.ui.custom.ReceiveGoldDialog;
import com.ydys.elsbballs.ui.custom.SignTodayDialog;
import com.ydys.elsbballs.util.AppContextUtil;
import com.ydys.elsbballs.util.MatrixUtils;
import com.ydys.elsbballs.util.TTAdManagerHolder;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class MakeMoneyFragment extends BaseFragment implements IBaseView, ReceiveGoldDialog.GoldDialogListener, SignTodayDialog.SignDayListener, LoginDialog.LoginListener {

    private static int BIND_PHONE_CODE = 1000;

    private static int FILL_IN_CODE = 1001;

    private static int BODY_DATA_CODE = 1002;

    private static int CASH_CODE = 1003;

    private static int INVITE_CODE = 1004;

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

    private SignDayAdapter signDayAdapter;

    private TaskInfoAdapter taskInfoAdapter;

    private TaskInfoPresenterImp taskInfoPresenterImp;

    private ReceiveGoldDialog receiveGoldDialog;

    private Handler mHandler = new Handler();

    private int currentTaskIndex;

    private UMShareAPI mShareAPI = null;

    private ProgressDialog progressDialog = null;

    private UserInfoPresenterImp userInfoPresenterImp;

    private TakeGoldInfoPresenterImp takeGoldInfoPresenterImp;

    private SignInfoPresenterImp signInfoPresenterImp;

    private SignTodayDialog signTodayDialog;

    private int currentDoubleDayIndex;

    private TaskInfoWrapper.SignTaskInfo signTaskInfo;

    private String taskType;

    //标识是否是登录过的用户或者是游客(也算是登录状态，设备从未登录绑定过)
    private boolean isLogin = true;

    LoginDialog loginDialog;

    private int taskOrLogin;

    //广告配置
    private TTAdNative mTTAdNative;

    private TTAdDislike mTTAdDislike;

    private TTNativeExpressAd mTTAd;

    private View adView;

    private TTRewardVideoAd mttRewardVideoAd;

    private TaskInfo adTaskInfo;//当前的广告任务

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

    public void setTopViewBgColor() {
        Logger.i("top view color make money--->");
        //StatusBarUtil.setLightMode(getActivity());
        //StatusBarUtil.setColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.red), 0);
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
    }

    @Override
    public void initViews() {
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(getActivity());

        receiveGoldDialog = new ReceiveGoldDialog(getActivity(), R.style.gold_dialog);
        receiveGoldDialog.setGoldDialogListener(this);

        loginDialog = new LoginDialog(getActivity(), R.style.common_dialog);
        loginDialog.setLoginListener(this);
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
                if (App.mUserInfo == null || (!App.isLogin && App.mUserInfo != null && App.mUserInfo.getIsBind() == 1)) {
                    if (loginDialog != null && !loginDialog.isShowing()) {
                        loginDialog.show();
                    }
                    return;
                }
                if (view.getId() == R.id.iv_double_icon) {
                    taskType = "sign_today";
                    currentDoubleDayIndex = position;
                    Logger.i("点击了翻倍--->" + position);
                    //todayDouble(signDayAdapter.getData().get(position).getDay());

                    doubleGoldVideo();
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
                if ((view.getId() == R.id.btn_get_now || view.getId() == R.id.get_now_gif) && taskInfo.getState() < 3) {

                    //联系点击太快，只接受一次事件
                    if (!AppContextUtil.isNotFastClick()) {
                        return;
                    }

                    if (taskInfo.getTaskType().equals("ad")) {
                        if (App.mUserInfo == null || (!App.isLogin && App.mUserInfo != null && App.mUserInfo.getIsBind() == 1)) {
                            if (loginDialog != null && !loginDialog.isShowing()) {
                                loginDialog.show();
                            }
                            return;
                        }
                        taskType = "ad";
                        adTaskInfo = taskInfo;

                        closeGoldDialog();
                        if (mttRewardVideoAd != null) {
                            //step6:在获取到广告后展示
                            mttRewardVideoAd.showRewardVideoAd(getActivity(), TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "home_video_ad");
                            //mttRewardVideoAd = null;
                        } else {
                            Logger.i("请先加载广告");
                        }
                        return;
                    }

                    if (!App.isLogin) {
                        if (loginDialog != null && !loginDialog.isShowing()) {
                            loginDialog.show();
                        }
                        return;
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
                            startActivityForResult(intent, INVITE_CODE);
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

        if (App.mUserInfo == null || (!App.isLogin && App.mUserInfo != null && App.mUserInfo.getIsBind() == 1)) {
            isLogin = false;
        } else {
            isLogin = true;
        }
        taskInfoPresenterImp.taskList(App.mUserInfo != null ? App.mUserInfo.getId() : "", isLogin ? 1 : 0);
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
        setTopViewBgColor();
        taskType = "";
        userInfoPresenterImp.imeiLogin(PhoneUtils.getIMEI(), App.agentId, "1",App.newStepNum);

        if (App.mUserInfo == null || (!App.isLogin && App.mUserInfo != null && App.mUserInfo.getIsBind() == 1)) {
            isLogin = false;
        } else {
            isLogin = true;
        }

        //taskInfoPresenterImp.taskList(App.mUserInfo != null ? App.mUserInfo.getId() : "", isLogin ? 1 : 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadExpressAd(Constants.BANNER_CODE_ID);
        loadVideoAd(Constants.VIDEO_CODE_ID, TTAdConstant.VERTICAL);
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
                Logger.i("load success task info");
                if (((TaskInfoWrapperRet) tData).getData() != null) {
                    signTaskInfo = ((TaskInfoWrapperRet) tData).getData().getSignTaskInfo();
                    mSignDaysTv.setText(signTaskInfo.getContinueNum() + "天");
                    mTomorrowGoldTv.setText(signTaskInfo.getTomorrowGold() + "");

                    if (App.mUserInfo == null || (!App.isLogin && App.mUserInfo != null && App.mUserInfo.getIsBind() == 1)) {
                        isLogin = false;
                    } else {
                        isLogin = true;
                    }

                    //判断邀请码是否填写完成
                    for (TaskInfo item : ((TaskInfoWrapperRet) tData).getData().getTaskList()) {
                        if (item.getTaskType().equals("invite_code") && item.getState() > 1) {
                            SPUtils.getInstance().put(Constants.INVITE_WRITE_CODE, true);
                        }
                    }

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

                        signTodayDialog.updateTodayInfo(signTaskInfo.getContinueNum() + "", currentGold + "", signTaskInfo.getGold() + "", MatrixUtils.getPrecisionMoney(money), adView);
                        loadExpressAd(Constants.BANNER_CODE_ID);
                    }
                }
            }

            if (tData instanceof UserInfoRet) {
                Logger.i("load success user info");
                if (((UserInfoRet) tData).getCode() == Constants.SUCCESS) {
                    SPUtils.getInstance().put(Constants.USER_INFO, JSONObject.toJSONString(((UserInfoRet) tData).getData()));
                    App.mUserInfo = ((UserInfoRet) tData).getData();

                    if (taskType.equals("bind_sj") || taskType.equals("bind_wx")) {
                        SPUtils.getInstance().put(Constants.LOCAL_LOGIN, true);
                        App.isLogin = true;
                        Toasty.normal(getActivity(), taskOrLogin == 1 ? "登录成功" : "绑定成功").show();
                        if (App.mUserInfo == null || (!App.isLogin && App.mUserInfo != null && App.mUserInfo.getIsBind() == 1)) {
                            isLogin = false;
                        } else {
                            isLogin = true;
                        }
                    }

                    if (App.mUserInfo == null || (!App.isLogin && App.mUserInfo != null && App.mUserInfo.getIsBind() == 1)) {
                        mTotalGoldNumTv.setText("0");
                        mMoneyTv.setText("≈0元");
                    } else {
                        mTotalGoldNumTv.setText(App.mUserInfo.getGold() + "");
                        mMoneyTv.setText("≈" + MatrixUtils.getPrecisionMoney(App.mUserInfo.getAmount()) + "元");
                    }

                    taskInfoPresenterImp.taskList(App.mUserInfo != null ? App.mUserInfo.getId() : "", isLogin ? 1 : 0);
                } else {
                    Toasty.normal(getActivity(), ((UserInfoRet) tData).getMsg()).show();
                }
            }

            if (tData instanceof TakeGoldInfoRet) {
                if (((TakeGoldInfoRet) tData).getCode() == Constants.SUCCESS) {
                    TakeGoldInfo tempGoldInfo = ((TakeGoldInfoRet) tData).getData();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (taskType.equals("invite_code")) {
                                SPUtils.getInstance().put(Constants.INVITE_WRITE_CODE, true);
                            }

                            if (receiveGoldDialog == null) {
                                receiveGoldDialog = new ReceiveGoldDialog(getActivity(), R.style.gold_dialog);
                                receiveGoldDialog.setGoldDialogListener(MakeMoneyFragment.this);
                            }

                            receiveGoldDialog.show();
                            receiveGoldDialog.updateGoldInfo(tempGoldInfo.getRequestGold() + "", tempGoldInfo.getGold() + "", "≈" + MatrixUtils.getPrecisionMoney(tempGoldInfo.getAmount()) + "元", adView);
                            loadExpressAd(Constants.BANNER_CODE_ID);
                        }
                    }, 100);
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
        receiveGoldDialog.updateGoldInfo(currentGold + "", totalGold + "", "≈" + MatrixUtils.getPrecisionMoney(money) + "元", adView);
        loadExpressAd(Constants.BANNER_CODE_ID);
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
            if (App.mUserInfo == null || (!App.isLogin && App.mUserInfo != null && App.mUserInfo.getIsBind() == 1)) {
                isLogin = false;
            } else {
                isLogin = true;
            }
            taskInfoPresenterImp.taskList(App.mUserInfo != null ? App.mUserInfo.getId() : "", isLogin ? 1 : 0);
        }

        //关闭广告弹窗时，移除adView
        if (adView != null && adView.getParent() != null) {
            ((ViewGroup) adView.getParent()).removeView(adView);
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
        closeGoldDialog();

        if (mttRewardVideoAd != null) {
            currentDoubleDayIndex = signTaskInfo.getContinueNum() - 1 >= 0 ? signTaskInfo.getContinueNum() - 1 : 0;
            taskType = "sign_today";
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(getActivity(), TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "home_video_ad");
            //mttRewardVideoAd = null;
        } else {
            Logger.i("请先加载广告");
        }
    }

    @Override
    public void wxLogin() {
        taskOrLogin = 1;
        taskType = "bind_wx";
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
        mShareAPI.getPlatformInfo(getActivity(), SHARE_MEDIA.WEIXIN, authListener);
    }

    @Override
    public void phoneLogin() {
        taskType = "bind_sj";
        Intent intent = new Intent(getActivity(), PhoneLoginActivity.class);
        startActivityForResult(intent, BIND_PHONE_CODE);
    }

    //广告的加载
    private void loadExpressAd(String codeId) {
        if (adView != null) {
            adView = null;
        }
        //mExpressContainer.removeAllViews();
        float expressViewWidth = 300;
        float expressViewHeight = 145;

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(expressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响模板广告的size
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("load error : " + code + ", " + message);
                //mExpressContainer.removeAllViews();
                if (adView != null) {
                    adView = null;
                }
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
//                mTTAd.setSlideIntervalTime(30*1000);
                bindAdListener(mTTAd);
                startTime = System.currentTimeMillis();
                mTTAd.render();
            }
        });
    }

    private long startTime = 0;

    private boolean mHasShowDownloadActive = false;

    private void bindAdListener(TTNativeExpressAd ad) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Logger.i("广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Logger.i("广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Logger.i("ExpressView", "render fail:" + (System.currentTimeMillis() - startTime));
                Logger.i(msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("ExpressView", "render suc:" + (System.currentTimeMillis() - startTime));
                //返回view的宽高 单位 dp
                Logger.i("渲染成功" + view.getWidth() + "---height--->" + view.getHeight() + "---title--->");

                //mExpressContainer.removeAllViews();
                //mExpressContainer.addView(view);
                if (adView != null) {
                    adView = null;
                }

                adView = view;
            }
        });
        //dislike设置
        bindDislike(ad, false);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {
                Logger.i("点击开始下载");
            }

            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true;
                    Logger.i("下载中，点击暂停");
                }
            }

            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                Logger.i("下载暂停，点击继续");
            }

            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                Logger.i("下载失败，点击重新下载");
            }

            @Override
            public void onInstalled(String fileName, String appName) {
                Logger.i("安装完成，点击图片打开");
            }

            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                Logger.i("点击安装");
            }
        });
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private void bindDislike(TTNativeExpressAd ad, boolean customStyle) {
        if (customStyle) {
            //使用自定义样式
            List<FilterWord> words = ad.getFilterWords();
            if (words == null || words.isEmpty()) {
                return;
            }

            final DislikeDialog dislikeDialog = new DislikeDialog(getActivity(), words);
            dislikeDialog.setOnDislikeItemClick(new DislikeDialog.OnDislikeItemClick() {
                @Override
                public void onItemClick(FilterWord filterWord) {
                    //屏蔽广告
                    Logger.i("点击 " + filterWord.getName());
                    //用户选择不喜欢原因后，移除广告展示
                    //mExpressContainer.removeAllViews();
                }
            });
            ad.setDislikeDialog(dislikeDialog);
            return;
        }
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(getActivity(), new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Logger.i("点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                //mExpressContainer.removeAllViews();
            }

            @Override
            public void onCancel() {
                Logger.i("点击取消 ");
            }
        });
    }

    private void loadVideoAd(String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setAdCount(1)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(1)  //奖励的数量
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("code" + code + "---" + message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                Logger.i("rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                Logger.i("rewardVideoAd loaded");
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Logger.i("rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Logger.i("rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Logger.i("rewardVideoAd close");
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        Logger.i("rewardVideoAd complete");
                        //缓存下一个视频
                        loadVideoAd(Constants.VIDEO_CODE_ID, TTAdConstant.VERTICAL);
                    }

                    @Override
                    public void onVideoError() {
                        Logger.i("rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        Logger.i("verify:" + rewardVerify + " amount:" + rewardAmount + " name:" + rewardName);
                        //视频看完，请求翻倍奖励

                        if (signTaskInfo != null && taskType.equals("sign_today")) {
                            int index = signTaskInfo.getContinueNum() - 1;
                            String day = signDayAdapter.getData().get(index).getDay();
                            todayDouble(day);
                        }

                        if (adTaskInfo != null && taskType.equals("ad")) {
                            taskGetGold(adTaskInfo);
                        }
                    }

                    @Override
                    public void onSkippedVideo() {
                        Logger.i("rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            Logger.i("下载中，点击下载区域暂停");
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        Logger.i("下载暂停，点击下载区域继续");
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        Logger.i("下载失败，点击下载区域重新下载");
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        Logger.i("下载完成，点击下载区域重新下载");
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        Logger.i("安装完成，点击下载区域打开");
                    }
                });
            }
        });
    }

}
