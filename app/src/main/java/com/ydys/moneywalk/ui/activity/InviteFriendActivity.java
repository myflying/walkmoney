package com.ydys.moneywalk.ui.activity;

import android.os.Bundle;

import com.jaeger.library.StatusBarUtil;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.custom.ActRuleDialog;

import butterknife.OnClick;

public class InviteFriendActivity extends BaseActivity {

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

    }

    @OnClick(R.id.layout_act_rule)
    void actRule() {
        if (actRuleDialog != null && !actRuleDialog.isShowing()) {
            actRuleDialog.show();
        }
    }
}
