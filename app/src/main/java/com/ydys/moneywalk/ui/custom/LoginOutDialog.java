package com.ydys.moneywalk.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydys.moneywalk.R;


public class LoginOutDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    LinearLayout mConfigLayout;

    LinearLayout mCancelLayout;

    TextView mTitleTv;

    TextView mContentTv;

    public interface LoginOutListener {
        void configLoginOut();

        void cancelLoginOut();
    }

    public LoginOutListener loginOutListener;

    public void setLoginOutListener(LoginOutListener loginOutListener) {
        this.loginOutListener = loginOutListener;
    }

    public LoginOutDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LoginOutDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginout_dialog_view);
        mTitleTv = findViewById(R.id.tv_config_title);
        mContentTv = findViewById(R.id.tv_content);

        mConfigLayout = findViewById(R.id.layout_config);
        mCancelLayout = findViewById(R.id.layout_cancel);
        mConfigLayout.setOnClickListener(this);
        mCancelLayout.setOnClickListener(this);

        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setDialogInfo(String title, String content) {
        mTitleTv.setText(title);
        mContentTv.setText(content);
    }

    private void initView() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_config:
                loginOutListener.configLoginOut();
                dismiss();
                break;
            case R.id.layout_cancel:
                loginOutListener.cancelLoginOut();
                dismiss();
                break;
            default:
                break;
        }
    }
}