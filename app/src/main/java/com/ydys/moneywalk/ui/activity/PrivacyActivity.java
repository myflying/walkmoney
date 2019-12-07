package com.ydys.moneywalk.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.just.agentweb.AgentWeb;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.presenter.Presenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2017/4/20.
 */

public class PrivacyActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.iv_back)
    ImageView mBackImageView;

    @BindView(R.id.layout_ad)
    LinearLayout mAdLayout;

    AgentWeb mAgentWeb;

    String[] titles = {"服务协议", "隐私政策"};

    private String webUrl;

    private int showType;

    private String homeUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_privacy;
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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            showType = bundle.getInt("show_type", 0);
            homeUrl = bundle.getString("web_url");
        }
        if (App.initInfo != null) {
            webUrl = showType == 0 ? App.initInfo.getAppConfig().getAgreement() : App.initInfo.getAppConfig().getPrivacy();
            mTitleTv.setTextColor(ContextCompat.getColor(this, R.color.black));
            mTitleTv.setText(titles[showType]);
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(mAdLayout, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(webUrl);
        }else{
            mTitleTv.setTextColor(ContextCompat.getColor(this, R.color.black));
            mTitleTv.setText(titles[showType]);
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(mAdLayout, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(homeUrl);
        }
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }
}
