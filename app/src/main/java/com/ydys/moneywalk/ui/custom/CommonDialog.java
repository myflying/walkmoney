package com.ydys.moneywalk.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydys.moneywalk.R;


public class CommonDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    LinearLayout mConfigLayout;

    LinearLayout mCancelLayout;

    TextView mTitleTv;

    TextView mContentTv;

    public interface CommonDialogListener {
        void commonConfig();

        void commonCancel();
    }

    public CommonDialogListener commonDialogListener;

    public void setCommonDialogListener(CommonDialogListener commonDialogListener) {
        this.commonDialogListener = commonDialogListener;
    }

    public CommonDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommonDialog(Context context, int themeResId) {
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
                commonDialogListener.commonConfig();
                dismiss();
                break;
            case R.id.layout_cancel:
                commonDialogListener.commonCancel();
                dismiss();
                break;
            default:
                break;
        }
    }
}