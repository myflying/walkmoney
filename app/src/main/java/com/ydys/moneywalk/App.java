package com.ydys.moneywalk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.ydys.moneywalk.bean.InitInfo;
import com.ydys.moneywalk.bean.UserInfo;

/**
 * Created by admin on 2017/4/7.
 */

public class App extends Application {

    protected static App mInstance;

    public static Context applicationContext;

    public static UserInfo mUserInfo;

    public static boolean isLogin;

    public static int newStepNum;//最新的步数

    public static Context getContext() {
        return applicationContext;
    }

    public App() {
        mInstance = this;
    }

    private int appCount = 0;

    public static int userTodayStep;//开启APP后接口获取到的用户当天的步数

    public static String agentId = "1";

    public static InitInfo initInfo;

    public static App getApp() {
        if (mInstance != null && mInstance instanceof App) {
            return (App) mInstance;
        } else {
            mInstance = new App();
            mInstance.onCreate();
            return (App) mInstance;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;

//        if(Build.VERSION.SDK_INT >= 24) {
//            Builder builder = new Builder();
//            StrictMode.setVmPolicy(builder.build());
//        }

        UMConfigure.init(this,"5ddf398e0cafb2d41b00066a",App.agentId,UMConfigure.DEVICE_TYPE_PHONE, "");
        // 选用LEGACY_AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        PlatformConfig.setWeixin("wx6b748df21345e04d", "57b47c686a85e63401b3ecb9ae80f5eb");

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                appCount++;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                appCount--;
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
        loadUserInfo();
    }

    /**
     * app是否在前台
     * @return true前台，false后台
     */
    public boolean isForeground(){
        return appCount > 0;
    }


    public void loadUserInfo() {
//        if (!StringUtils.isEmpty(SPUtils.getInstance().getString(Constants.USER_INFO))) {
//            Logger.i(SPUtils.getInstance().getString(Constants.USER_INFO));
//            mUserInfo = JSON.parseObject(SPUtils.getInstance().getString(Constants.USER_INFO), new TypeReference<UserInfo>() {
//            });
//            App.isLogin = true;
//        }
    }
}
