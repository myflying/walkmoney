package com.ydys.moneywalk.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ydys.moneywalk.R;

/**
 * 超出限制
 */
public class LoginDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private ImageView mCloseIv;

    LinearLayout mWxLoginLayout;

    LinearLayout mPhoneLoginLayout;

    public interface LoginListener {
        void wxLogin();

        void phoneLogin();
    }

    public LoginListener loginListener;

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public LoginDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LoginDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mCloseIv = findViewById(R.id.iv_close);
        mWxLoginLayout = findViewById(R.id.layout_wx_login);
        mPhoneLoginLayout = findViewById(R.id.layout_phone_login);
        mCloseIv.setOnClickListener(this);
        mWxLoginLayout.setOnClickListener(this);
        mPhoneLoginLayout.setOnClickListener(this);

        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                this.dismiss();
                break;
            case R.id.layout_wx_login:
                this.loginListener.wxLogin();
                this.dismiss();
                break;
            case R.id.layout_phone_login:
                this.loginListener.phoneLogin();
                this.dismiss();
                break;
            default:
                break;
        }
    }
}