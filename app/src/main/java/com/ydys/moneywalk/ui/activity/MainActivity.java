package com.ydys.moneywalk.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMShareAPI;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.InitInfoRet;
import com.ydys.moneywalk.bean.MessageEvent;
import com.ydys.moneywalk.bean.UserInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.InitInfoPresenterImp;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.presenter.UserInfoPresenterImp;
import com.ydys.moneywalk.ui.adapter.MyFragmentAdapter;
import com.ydys.moneywalk.ui.custom.HongBaoDialog;
import com.ydys.moneywalk.ui.custom.PermissionDialog;
import com.ydys.moneywalk.ui.custom.PrivacyDialog;
import com.ydys.moneywalk.ui.custom.ReceiveDoubleGoldDialog;
import com.ydys.moneywalk.ui.custom.ReceiveGoldDialog;
import com.ydys.moneywalk.ui.fragment.HomeFragment;
import com.ydys.moneywalk.ui.fragment.MakeMoneyFragment;
import com.ydys.moneywalk.ui.fragment.MyFragment;
import com.ydys.moneywalk.ui.fragment.TestFragment;
import com.ydys.moneywalk.view.UserInfoView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity implements IBaseView, PrivacyDialog.PrivacyListener, PermissionDialog.PermissionConfigListener {

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private long clickTime = 0;

    private final int[] TITLES = new int[]{R.string.tab_home_txt, R.string.tab_money_txt, R.string.tab_my_txt};

    private final int[] IMAGES = new int[]{R.drawable.tab_home, R.drawable.tab_money, R.drawable.tab_my};

    private ArrayList<Fragment> mFragmentList = new ArrayList<>();

    private MyFragmentAdapter adapter;

    PrivacyDialog privacyDialog;

    HongBaoDialog hongBaoDialog;

    ReceiveDoubleGoldDialog receiveDoubleGoldDialog;

    ReceiveGoldDialog receiveGoldDialog;

    UserInfoPresenterImp userInfoPresenterImp;

    InitInfoPresenterImp initInfoPresenterImp;

    PermissionDialog permissionDialog;

    private Handler mHandler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void initVars() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    void showReadPhone() {
        MainActivityPermissionsDispatcher.showReadStorageWithPermissionCheck(this);
    }

    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    void onReadPhoneDenied() {
        //Toast.makeText(this, R.string.permission_storage_denied, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        }
    }

    @OnShowRationale(Manifest.permission.READ_PHONE_STATE)
    void showRationaleForReadPhone(PermissionRequest request) {
        //Toast.makeText(this, R.string.permission_read_phone_rationale, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        } else {
            request.proceed();
        }
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    void onReadPhoneNeverAskAgain() {
        //Toast.makeText(this, R.string.permission_storage_never_ask_again, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        }
    }

    //读写本地存储权限
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showReadStorage() {
        MainActivityPermissionsDispatcher.showReadLocationWithPermissionCheck(this);
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onReadStorageDenied() {
        //Toast.makeText(this, R.string.permission_storage_denied, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        }
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForReadStorage(PermissionRequest request) {
        //Toast.makeText(this, R.string.permission_read_phone_rationale, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        } else {
            request.proceed();
        }
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onReadStorageNeverAskAgain() {
        //Toast.makeText(this, R.string.permission_storage_never_ask_again, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        }
    }


    //读取地理位置权限
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showReadLocation() {
        readPhoneTask();
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onReadLocationDenied() {
        //Toast.makeText(this, R.string.permission_storage_denied, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        }
    }

    @OnShowRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
    void showRationaleForReadLocation(PermissionRequest request) {
        //Toast.makeText(this, R.string.permission_read_phone_rationale, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        } else {
            request.proceed();
        }
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_COARSE_LOCATION)
    void onReadLocationNeverAskAgain() {
        //Toast.makeText(this, R.string.permission_storage_never_ask_again, Toast.LENGTH_SHORT).show();
        if (permissionDialog != null && !permissionDialog.isShowing()) {
            permissionDialog.show();
        }
    }

    @Override
    protected void initViews() {
        mFragmentList.add(new HomeFragment());
        mFragmentList.add(new MakeMoneyFragment());
        mFragmentList.add(new MyFragment());

        setTabs(tabLayout, this.getLayoutInflater(), TITLES, IMAGES);
        adapter = new MyFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //绑定tab点击事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                Logger.i("tab pos --->" + pos);
                viewPager.setCurrentItem(pos);
                if (pos == 0) {
                    if (mFragmentList.get(pos) instanceof HomeFragment) {
                        ((HomeFragment) mFragmentList.get(pos)).setTopViewBgColor();
                    }
                }
                if (pos == 1) {
                    if (mFragmentList.get(pos) instanceof MakeMoneyFragment) {
                        ((MakeMoneyFragment) mFragmentList.get(pos)).makeMoneySelect();
                    }
                }
                if (pos == 2) {
                    if (mFragmentList.get(pos) instanceof MyFragment) {
                        ((MyFragment) mFragmentList.get(pos)).loadUserInfo();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        userInfoPresenterImp = new UserInfoPresenterImp(this, this);
        initInfoPresenterImp = new InitInfoPresenterImp(this, this);

        hongBaoDialog = new HongBaoDialog(this, R.style.common_dialog);

        privacyDialog = new PrivacyDialog(this, R.style.common_dialog);
        privacyDialog.setPrivacyListener(this);

        permissionDialog = new PermissionDialog(this, R.style.common_dialog);
        permissionDialog.setPermissionConfigListener(this);

        if (!SPUtils.getInstance().getBoolean(Constants.IS_AGREE_PRIVACY, false)) {
            privacyDialog.show();
        } else {
            MainActivityPermissionsDispatcher.showReadPhoneWithPermissionCheck(this);
        }

        receiveDoubleGoldDialog = new ReceiveDoubleGoldDialog(this, R.style.common_dialog);
//        receiveDoubleGoldDialog.show();

        receiveGoldDialog = new ReceiveGoldDialog(this, R.style.common_dialog);
        //receiveGoldDialog.show();
    }

    public void setTabs(TabLayout tabLayout, LayoutInflater layoutInflater, int[] titles, int[] images) {
        for (int i = 0; i < titles.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            View view = layoutInflater.inflate(R.layout.tab_custom, null);
            tab.setCustomView(view);
            TextView tabText = view.findViewById(R.id.tv_tab);
            tabText.setText(titles[i]);
            ImageView tabImage = view.findViewById(R.id.iv_tab);
            tabImage.setImageResource(images[i]);
            tabLayout.addTab(tab);
        }
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    public void readPhoneTask() {
        Logger.i("readPhoneTask--->" + PhoneUtils.getIMEI());
        userInfoPresenterImp.imeiLogin(PhoneUtils.getIMEI(), App.agentId, "1",0);
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            clickTime = System.currentTimeMillis();
            Toasty.normal(getApplicationContext(), "再按一次退出").show();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(Object tData) {

        if (tData != null) {
            if (tData instanceof UserInfoRet && ((UserInfoRet) tData).getCode() == Constants.SUCCESS) {
                Logger.i("user info --->" + JSON.toJSONString(tData));
                if (((UserInfoRet) tData).getData() != null) {
                    App.mUserInfo = ((UserInfoRet) tData).getData();
                    initInfoPresenterImp.initInfo(((UserInfoRet) tData).getData().getId());
                }
            }

            if (tData instanceof InitInfoRet && ((InitInfoRet) tData).getCode() == Constants.SUCCESS) {
                Logger.i("init info--->" + JSON.toJSONString(tData));
                if (((InitInfoRet) tData).getData() != null) {
                    App.initInfo = ((InitInfoRet) tData).getData();
                    App.userTodayStep = App.initInfo.getUserStepData().getStepNum();
                    Logger.i("user today step--->" + App.userTodayStep);
                }

                MessageEvent messageEvent = new MessageEvent("init_success");
                EventBus.getDefault().post(messageEvent);

                if (hongBaoDialog != null && !hongBaoDialog.isShowing()) {
                    if (App.mUserInfo.getShowNew() == 1) {
                        hongBaoDialog.show();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hongBaoDialog.autoOpenHongBao(App.initInfo.getNewTaskConfig().getGold());
                            }
                        }, 2000);
                    }
                }
            }
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void agree() {
        SPUtils.getInstance().put(Constants.IS_AGREE_PRIVACY, true);
        MainActivityPermissionsDispatcher.showReadPhoneWithPermissionCheck(this);
    }

    @Override
    public void notAgree() {
        Toasty.normal(this, "请你同意授权，否则将无法使用" + AppUtils.getAppName() + "APP功能").show();
    }

    @Override
    public void grantPermission() {
        MainActivityPermissionsDispatcher.showReadPhoneWithPermissionCheck(this);
    }
}
