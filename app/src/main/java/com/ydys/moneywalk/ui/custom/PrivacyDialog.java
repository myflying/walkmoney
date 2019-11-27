package com.ydys.moneywalk.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.ydys.moneywalk.R;


public class PrivacyDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mConfigLayout;

    private LinearLayout mNotAgreeLayout;

    public PrivacyListener privacyListener;

    public interface PrivacyListener {
        void agree();

        void notAgree();
    }

    public void setPrivacyListener(PrivacyListener privacyListener) {
        this.privacyListener = privacyListener;
    }

    public PrivacyDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public PrivacyDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mConfigLayout = findViewById(R.id.layout_config);
        mNotAgreeLayout = findViewById(R.id.layout_not_agree);

        mConfigLayout.setOnClickListener(this);
        mNotAgreeLayout.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_config:
                this.privacyListener.agree();
                this.dismiss();
                break;
            case R.id.layout_not_agree:
                this.privacyListener.notAgree();
                break;
            default:
                break;
        }
    }
}