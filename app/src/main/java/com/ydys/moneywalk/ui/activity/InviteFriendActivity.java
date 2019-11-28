package com.ydys.moneywalk.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.custom.ActRuleDialog;

import butterknife.BindView;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

public class InviteFriendActivity extends BaseActivity {

    @BindView(R.id.wx_invite)
    GifImageView mWxInviteGif;

    @BindView(R.id.tv_invite_code)
    TextView mInviteCodeTv;

    @BindView(R.id.tv_copy)
    TextView mCopyTv;

    ActRuleDialog actRuleDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {
        actRuleDialog = new ActRuleDialog(this, R.style.common_dialog);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mInviteCodeTv.setText(App.mUserInfo != null ? App.mUserInfo.getId() : "");
    }

    @OnClick(R.id.layout_act_rule)
    void actRule() {
        if (actRuleDialog != null && !actRuleDialog.isShowing()) {
            actRuleDialog.show();
        }
    }

    @OnClick(R.id.tv_copy)
    void copyInviteCode() {
        copyTextToSystem(this, App.mUserInfo != null ? App.mUserInfo.getId() : "");
        ToastUtils.showLong("已复制");
    }

    /**
     * 复制文本到系统剪切板
     */
    public static void copyTextToSystem(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", text);
        cm.setPrimaryClip(mClipData);
    }

    @OnClick(R.id.wx_invite)
    void wxInvite() {

    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }
}
