package com.ydys.moneywalk.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ydys.moneywalk.R;

/**
 * 超出限制
 */
public class ExceedDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private ImageView mCloseIv;

    public ExceedDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ExceedDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exceed_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mCloseIv = findViewById(R.id.iv_close);
        mCloseIv.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                this.dismiss();
                break;
            default:
                break;
        }
    }
}