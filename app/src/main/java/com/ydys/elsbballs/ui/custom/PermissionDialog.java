package com.ydys.elsbballs.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.ydys.elsbballs.R;

/**
 * 授权管理
 */
public class PermissionDialog extends Dialog implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mGrantPerLayout;

    public interface PermissionConfigListener {
        void grantPermission();
    }

    public PermissionConfigListener permissionConfigListener;

    public void setPermissionConfigListener(PermissionConfigListener permissionConfigListener) {
        this.permissionConfigListener = permissionConfigListener;
    }

    public PermissionDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public PermissionDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_dialog_view);
        initView();
    }

    private void initView() {
        mGrantPerLayout = findViewById(R.id.layout_grant);
        mGrantPerLayout.setOnClickListener(this);
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
            case R.id.layout_grant:
                permissionConfigListener.grantPermission();
                this.dismiss();
                break;
            default:
                break;
        }
    }
}