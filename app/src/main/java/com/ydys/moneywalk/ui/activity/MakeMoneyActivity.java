package com.ydys.moneywalk.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.custom.ActRuleDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class MakeMoneyActivity extends BaseActivity {

    @BindView(R.id.iv_make_money)
    SubsamplingScaleImageView mMakeMoneyIv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_make_money;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    protected void initVars() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (App.initInfo != null) {
            Logger.i(App.initInfo.getAppConfig().getStrategy());

            Glide.with(context)
                    .download(Constants.BASE_IMAGE_URL + App.initInfo.getAppConfig().getStrategy())
                    .into(new SimpleTarget<File>() {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            Log.d("load failed", "nothing");
                        }

                        @Override
                        public void onResourceReady(File resource, Transition<? super File> transition) {
                            mMakeMoneyIv.setImage(ImageSource.uri(resource.getAbsolutePath()));
                            mMakeMoneyIv.setMaxScale(10f);
                        }
                    });

        }
    }

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }
}
