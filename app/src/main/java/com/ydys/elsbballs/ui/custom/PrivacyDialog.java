package com.ydys.elsbballs.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydys.elsbballs.R;
import com.ydys.elsbballs.ui.activity.PrivacyActivity;


public class PrivacyDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mConfigLayout;

    private LinearLayout mNotAgreeLayout;

    public PrivacyListener privacyListener;

    TextView mXieYiTv;

    TextView mPrivacyTv;

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
        mXieYiTv = findViewById(R.id.tv_xieyi);
        mPrivacyTv = findViewById(R.id.tv_privacy);

        mXieYiTv.setOnClickListener(this);
        mPrivacyTv.setOnClickListener(this);
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
            case R.id.tv_xieyi:
                Intent intent = new Intent(mContext, PrivacyActivity.class);
                intent.putExtra("show_type", 0);
                intent.putExtra("web_url","http:\\/\\/zlb.zhanyu55.com\\/html\\/service.html");
                mContext.startActivity(intent);
                break;
            case R.id.tv_privacy:
                Intent intent1 = new Intent(mContext, PrivacyActivity.class);
                intent1.putExtra("show_type", 1);
                intent1.putExtra("web_url","http:\\/\\/zlb.zhanyu55.com\\/html\\/privacy.html");
                mContext.startActivity(intent1);
                break;
            default:
                break;
        }
    }
}