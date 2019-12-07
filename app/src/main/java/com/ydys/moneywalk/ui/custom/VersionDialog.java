package com.ydys.moneywalk.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;


public class VersionDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    ImageView mCloseIv;

    TextView mVersionNameTv;

    TextView mContentTv;

    Button mUpdateBtn;

    FrameLayout mProgressLayout;

    ProgressBar progressBar;

    TextView mProgressTv;

    private int isForceUpdate;//0:不强制，1：强制

    private String versionName;

    private String versionContent;

    public void setIsForceUpdate(int isForceUpdate) {
        this.isForceUpdate = isForceUpdate;
    }

    public interface VersionListener {
        void versionUpdate();

        void cancelUpdate();
    }

    public VersionListener versionListener;

    public void setVersionListener(VersionListener versionListener) {
        this.versionListener = versionListener;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public void setVersionContent(String versionContent) {
        this.versionContent = versionContent;
    }

    public VersionDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public VersionDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.version_dialog_view);
        initView();
    }

    private void initView() {
        mCloseIv = findViewById(R.id.iv_close);
        mVersionNameTv = findViewById(R.id.tv_version_name);
        mContentTv = findViewById(R.id.tv_update_content);
        mUpdateBtn = findViewById(R.id.btn_update);
        mProgressLayout = findViewById(R.id.layout_progress);
        progressBar = findViewById(R.id.progress_bar);
        mProgressTv = findViewById(R.id.tv_progress);

        mUpdateBtn.setOnClickListener(this);
        mCloseIv.setOnClickListener(this);
        mCloseIv.setVisibility(isForceUpdate == 1 ? View.GONE : View.VISIBLE);
        setCanceledOnTouchOutside(isForceUpdate == 1 ? false : true);
        if (!StringUtils.isEmpty(versionName)) {
            mVersionNameTv.setText("V" + versionName);
        }
        if (!StringUtils.isEmpty(versionContent)) {
            mContentTv.setText(Html.fromHtml(versionContent));
        }
    }

    public void updateProgress(int progress) {
        progressBar.setProgress(progress);
        mProgressTv.setText(progress + "%");
        int xWidth = 640 / 100 * progress;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(xWidth, 0, 0, 0);
        mProgressTv.setLayoutParams(params);
    }

    public void downFinish() {
        mUpdateBtn.setVisibility(View.VISIBLE);
        mProgressLayout.setVisibility(View.GONE);
        mUpdateBtn.setText("立即安装");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (isForceUpdate == 0) {
                    this.versionListener.cancelUpdate();
                    this.dismiss();
                }
                break;
            case R.id.btn_update:
                mUpdateBtn.setVisibility(View.GONE);
                mProgressLayout.setVisibility(View.VISIBLE);
                this.versionListener.versionUpdate();
                break;
            default:
                break;
        }
    }
}