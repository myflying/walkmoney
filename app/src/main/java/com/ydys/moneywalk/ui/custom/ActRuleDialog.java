package com.ydys.moneywalk.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;


public class ActRuleDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private ImageView mCloseIv;

    TextView mRuleTv;

    public ActRuleDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public ActRuleDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rule_dialog_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        mCloseIv = findViewById(R.id.iv_close);
        mRuleTv = findViewById(R.id.tv_rule_content);
        mCloseIv.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        mRuleTv.setText(Html.fromHtml(App.initInfo != null ? App.initInfo.getAppConfig().getInviteRule() : ""));
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