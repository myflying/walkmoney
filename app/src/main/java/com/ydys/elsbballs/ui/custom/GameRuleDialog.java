package com.ydys.elsbballs.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ydys.elsbballs.R;

public class GameRuleDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private ImageView mCloseIv;

    public interface  GameRuleListener{
        void gameRuleClose();
    }

    public GameRuleListener gameRuleListener;

    public void setGameRuleListener(GameRuleListener gameRuleListener) {
        this.gameRuleListener = gameRuleListener;
    }

    public GameRuleDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public GameRuleDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_rule_view);
        setCanceledOnTouchOutside(true);
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
                this.gameRuleListener.gameRuleClose();
                this.dismiss();
                break;
            default:
                break;
        }
    }
}