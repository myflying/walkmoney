package com.ydys.moneywalk.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.base.IBaseView;
import com.ydys.moneywalk.bean.VersionInfo;
import com.ydys.moneywalk.bean.VersionInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.presenter.VersionInfoPresenterImp;
import com.ydys.moneywalk.ui.custom.CommonDialog;
import com.ydys.moneywalk.ui.custom.VersionDialog;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class SettingActivity extends BaseActivity implements CommonDialog.CommonDialogListener, VersionDialog.VersionListener, IBaseView {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.tv_qq_qun)
    TextView mQQQunTv;

    @BindView(R.id.tv_cache)
    TextView mCacheTv;

    @BindView(R.id.tv_version_name)
    TextView mVersionNameTv;

    CommonDialog loginOutDialog;

    private VersionInfoPresenterImp versionInfoPresenterImp;

    VersionDialog versionDialog;

    private VersionInfo versionInfo;

    BaseDownloadTask task;

    private ProgressDialog progressDialog = null;

    private UMShareAPI mShareAPI = null;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 2:
                    int progress = (Integer) msg.obj;
                    versionDialog.updateProgress(progress);
                    break;
                case 3:
                    versionDialog.downFinish();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void initVars() {
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 0);
    }

    @Override
    protected void initViews() {
        FileDownloader.setup(this);
        mShareAPI = UMShareAPI.get(this);
        mTitleTv.setTextColor(ContextCompat.getColor(this, R.color.black));
        mTitleTv.setText("设置");

        loginOutDialog = new CommonDialog(this, R.style.common_dialog);
        loginOutDialog.setCommonDialogListener(this);

        versionDialog = new VersionDialog(this, R.style.common_dialog);
        versionDialog.setVersionListener(this);
        versionDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (versionInfo != null && versionInfo.getForceUpdate() == 1) {
                        return true;//不执行父类点击事件
                    }
                    return false;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.tv_cache)
    void clearCache() {
        CacheDiskUtils.getInstance().clear();
        ToastUtils.showLong("缓存已清除");
        mCacheTv.setVisibility(View.GONE);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (App.initInfo != null && App.initInfo.getAppConfig() != null) {
            mQQQunTv.setText(App.initInfo.getAppConfig().getMobile());
        }
        if (CacheDiskUtils.getInstance().getCacheSize() > 0) {
            mCacheTv.setVisibility(View.VISIBLE);
            mCacheTv.setText(CacheDiskUtils.getInstance().getCacheSize() + "k");
        } else {
            mCacheTv.setVisibility(View.GONE);
        }

        mVersionNameTv.setText("V" + AppUtils.getAppVersionName());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在查询");

        versionInfoPresenterImp = new VersionInfoPresenterImp(this, this);
    }

    @OnClick(R.id.layout_user_info)
    void userInfo() {
        Intent intent = new Intent(this, UserInfoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_about_us)
    void about() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_question)
    void question() {
        Intent intent = new Intent(this, QuestionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_login_out)
    void loginOut() {
        if (loginOutDialog != null && !loginOutDialog.isShowing()) {
            loginOutDialog.show();
        }
    }

    @OnClick(R.id.layout_check_version)
    void versionCheck() {
        versionInfoPresenterImp.updateVersion(App.agentId);
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }

    @Override
    public void commonConfig() {
        SPUtils.getInstance().put(Constants.USER_INFO, "");
        SPUtils.getInstance().remove(Constants.USER_INFO);
        SPUtils.getInstance().put(Constants.LOCAL_LOGIN, false);
        App.mUserInfo = null;
        App.isLogin = false;

        mShareAPI.deleteOauth(this, SHARE_MEDIA.WEIXIN, null);
        finish();
    }

    @Override
    public void commonCancel() {

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

        if (tData instanceof VersionInfoRet) {
            if (((VersionInfoRet) tData).getCode() == Constants.SUCCESS) {
                if (((VersionInfoRet) tData).getData() != null) {
                    versionInfo = ((VersionInfoRet) tData).getData();

                    int currentCode = AppUtils.getAppVersionCode();
                    if (versionInfo != null && versionInfo.getVersionCode() > currentCode) {
                        if (versionDialog != null && !versionDialog.isShowing()) {
                            versionDialog.setVersionName(versionInfo.getVersionNum());
                            versionDialog.setVersionContent(versionInfo.getUpdateContent());
                            versionDialog.setIsForceUpdate(versionInfo.getForceUpdate());
                            versionDialog.show();
                        }
                    } else {
                        Toasty.normal(this, "已经是最新版本").show();
                        //Logger.i("已经是最新版本--->" + currentCode);
                    }
                }
            } else {
                Toasty.normal(this, "版本检测失败").show();
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
    public void versionUpdate() {
        if (versionInfo != null && !StringUtils.isEmpty(versionInfo.getDownUrl())) {
            downAppFile(versionInfo.getDownUrl());
        }
    }

    @Override
    public void cancelUpdate() {

    }

    public void downAppFile(String downUrl) {
        Logger.i("down url --->" + downUrl);

        final String filePath = PathUtils.getExternalAppFilesPath() + "/new_walk.apk";
        Logger.i("down app path --->" + filePath);

        task = FileDownloader.getImpl().create(downUrl)
                .setPath(filePath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        //Toasty.normal(SettingActivity.this, "正在更新版本后...").show();
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        int progress = (int) ((soFarBytes * 1.0 / totalBytes) * 100);
                        Logger.i("progress--->" + soFarBytes + "---" + totalBytes + "---" + progress);

                        Message message = new Message();
                        message.what = 2;
                        message.obj = progress;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Toasty.normal(SettingActivity.this, "下载完成").show();
                        Message message = new Message();
                        message.what = 3;
                        mHandler.sendMessage(message);

                        AppUtils.installApp(filePath);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });

        task.start();
    }
}
