package com.ydys.moneywalk.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydys.moneywalk.R;

/**
 * 超出限制
 */
public class ExceedDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private ImageView mCloseIv;

    LinearLayout mSeeVideoLayout;

    LinearLayout mCancelLayout;

    public interface ExceedListener {
        void seeVideo();

        void cancelSeeVideo();
    }

    public ExceedListener exceedListener;

    public void setExceedListener(ExceedListener exceedListener) {
        this.exceedListener = exceedListener;
    }

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
        mSeeVideoLayout = findViewById(R.id.layout_see_video);
        mCancelLayout = findViewById(R.id.layout_cancel);

        mCloseIv = findViewById(R.id.iv_close);
        mCloseIv.setOnClickListener(this);
        mSeeVideoLayout.setOnClickListener(this);
        mCancelLayout.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_see_video:
                this.exceedListener.seeVideo();
                dismiss();
                break;
            case R.id.layout_cancel:
                this.exceedListener.cancelSeeVideo();
                dismiss();
                break;
            case R.id.iv_close:
                this.dismiss();
                break;
            default:
                break;
        }
    }
}