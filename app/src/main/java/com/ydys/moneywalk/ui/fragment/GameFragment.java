package com.ydys.moneywalk.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.orhanobut.logger.Logger;
import com.yc.h5.core.YCGameClickCallback;
import com.yc.h5.core.YcGameAdCallback;
import com.yc.h5.core.YcGameAdShowCallback;
import com.yc.h5.core.YcGameDataCallback;
import com.yc.h5.core.YcGameInfo;
import com.yc.h5.core.YcGameInitCallback;
import com.yc.h5.core.YcGamePlayTimeCallback;
import com.yc.h5.core.YcGameSDK;
import com.yc.h5.core.YcGameWebView;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.bean.TakeGoldInfo;
import com.ydys.moneywalk.bean.TakeGoldInfoRet;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.TakeGoldInfoPresenterImp;
import com.ydys.moneywalk.util.TTAdManagerHolder;
import com.ydys.moneywalk.view.TakeGoldInfoView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class GameFragment extends BaseFragment implements YCGameClickCallback, YcGameDataCallback, YcGamePlayTimeCallback, YcGameAdShowCallback, TakeGoldInfoView {

    public static String TAG = "GameFragment";

    @BindView(R.id.layout_game_content)
    FrameLayout mGameLayout;

    @BindView(R.id.tv_game_gold_one)
    TextView mGameGoldOneTv;

    @BindView(R.id.tv_game_gold_two)
    TextView mGameGoldTwoTv;

    @BindView(R.id.tv_game_gold_three)
    TextView mGameGoldThreeTv;

    @BindView(R.id.tv_game_gold_four)
    TextView mGameGoldFourTv;

    //广告配置
    private TTAdNative mTTAdNative;

    private TTAdDislike mTTAdDislike;

    private TTNativeExpressAd mTTAd;

    private TTRewardVideoAd mttRewardVideoAd;

    private View adView;

    private TakeGoldInfoPresenterImp takeGoldInfoPresenterImp;

    private int exChange = 10;

    private TextView[] goldTxs;

    @Override
    protected int getContentView() {
        return R.layout.fragment_game;
    }

    @Override
    public void initVars() {

        List<Animator> animators = new ArrayList<>();
        ObjectAnimator translationYAnim1 = ObjectAnimator.ofFloat(mGameGoldOneTv, "translationY", -12.0f, 12.0f, -12.0f);
        translationYAnim1.setDuration(2500);
        translationYAnim1.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim1.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim1);

        ObjectAnimator translationYAnim2 = ObjectAnimator.ofFloat(mGameGoldOneTv, "translationY", -12.0f, 12.0f, -12.0f);
        translationYAnim2.setDuration(2500);
        translationYAnim2.setRepeatCount(ValueAnimator.INFINITE);
        translationYAnim2.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(translationYAnim2);

        AnimatorSet btnSexAnimatorSet = new AnimatorSet();
        btnSexAnimatorSet.playTogether(animators);
        btnSexAnimatorSet.setStartDelay(100);
        btnSexAnimatorSet.start();
    }

    @Override
    public void initViews() {
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(getActivity());
        loadVideoAd(Constants.VIDEO_CODE_ID, TTAdConstant.VERTICAL);

        goldTxs = new TextView[]{mGameGoldOneTv, mGameGoldTwoTv, mGameGoldThreeTv, mGameGoldFourTv};

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        YcGameWebView ycGameWebView = new YcGameWebView(getActivity());
        mGameLayout.addView(ycGameWebView, params);

        YcGameSDK.getInstance().initGame(getActivity(), "yc_game", new YcGameInitCallback() {
            @Override
            public void gameInit() {
                YcGameInfo ycGameInfo = YcGameSDK.getInstance().getGameInfoByGameId("2048ball");
                if (ycGameInfo != null) {
                    if (ycGameInfo.getDirection() == 1) {
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    }
                    Log.d(TAG, "游戏url:" + ycGameInfo.getUrl());

                    ycGameWebView.post(new Runnable() {
                        @Override
                        public void run() {
                            ycGameWebView.loadUrl(ycGameInfo.getUrl());
                        }
                    });
                    return;
                } else {
                    Log.d(TAG, "游戏信息未获取");
                }
            }
        });

        YcGameSDK.getInstance().setGameAdShowCallback(this);
        YcGameSDK.getInstance().setGameClickCallback(this);
        YcGameSDK.getInstance().setGameDataCallback(this);
        YcGameSDK.getInstance().setGamePlayTimeCallback(this);
    }

    @Override
    public void loadData() {
        takeGoldInfoPresenterImp = new TakeGoldInfoPresenterImp(this, getActivity());
    }


    @Override
    public void gameClickCallback(String gameName, String gameID) {
        Log.d(TAG, "游戏点击:(" + gameName + "," + gameID + ")");
        Toast.makeText(getActivity(), "游戏点击:(" + gameName + "," + gameID + ")", Toast.LENGTH_LONG).show();
    }

    public int showedGold = 0;

    @Override
    public void gameDataCallback(String type, String num) {
        Log.d(TAG, "游戏数据:(" + type + "," + num + ")");
        //Toast.makeText(getActivity(), "游戏数据:(" + type + "," + num + ")", Toast.LENGTH_LONG).show();

        if (!StringUtils.isEmpty(num)) {
            int tempNum = Integer.parseInt(num);
            int goldNum = tempNum / exChange;
            Logger.i("goldNum--->" + goldNum);
            if (goldNum >= 1) {
                for (int i = 0; i < goldTxs.length; i++) {
                    if (goldTxs[i].getVisibility() == View.GONE) {
                        int nowNum = goldNum - showedGold;
                        showedGold = goldNum;
                        randomShowGold(goldTxs[i], nowNum);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void gamePlayTimeCallback(String gameId, int playTimeInSeconds) {
        Log.d(TAG, "游戏时间:(" + gameId + "," + playTimeInSeconds + ")");
        Toast.makeText(getActivity(), "游戏时间:(" + gameId + "," + playTimeInSeconds + ")", Toast.LENGTH_LONG).show();
    }

    @Override
    public void gameAdShow(YcGameAdCallback callback) {
        //播放视频完成后回调
        ycGameAdCallback = callback;
        //Log.d(TAG, "显示视频");
        //Toast.makeText(getActivity(), "显示视频", Toast.LENGTH_LONG).show();
        mGameLayout.post(new Runnable() {
            @Override
            public void run() {
                seeVideo();
            }
        });
    }

    public void randomShowGold(TextView showGoldTv, int goldNum) {
        showGoldTv.post(new Runnable() {
            @Override
            public void run() {
                showGoldTv.setText(goldNum + "");
                showGoldTv.setVisibility(View.VISIBLE);
            }
        });
    }

    public void seeVideo() {
        //ToastUtils.showLong("模拟视频看完，领取金币成功");
        if (mttRewardVideoAd != null) {
            //step6:在获取到广告后展示
            mttRewardVideoAd.showRewardVideoAd(getActivity(), TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "home_video_ad");
            //mttRewardVideoAd = null;
        } else {
            Logger.i("请先加载广告");
            loadVideoAd(Constants.VIDEO_CODE_ID, TTAdConstant.VERTICAL);
        }
    }

    @OnClick(R.id.tv_game_gold_one)
    void playVideo() {
        seeVideo();
    }

    @OnClick(R.id.tv_game_gold_two)
    void playTwoVideo() {
        seeVideo();
    }

    private long startTime = 0;

    private boolean mHasShowDownloadActive = false;

    YcGameAdCallback ycGameAdCallback;

    private void loadVideoAd(String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setAdCount(1)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(1)  //奖励的数量
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mTTAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Logger.i("code" + code + "---" + message);
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                Logger.i("rewardVideoAd video cached");
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                Logger.i("rewardVideoAd loaded");
                mttRewardVideoAd = ad;
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Logger.i("rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Logger.i("rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Logger.i("rewardVideoAd close");
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        Logger.i("rewardVideoAd complete");
                        //缓存下一个视频
                        loadVideoAd(Constants.VIDEO_CODE_ID, TTAdConstant.VERTICAL);
                    }

                    @Override
                    public void onVideoError() {
                        Logger.i("rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        Logger.i("verify:" + rewardVerify + " amount:" + rewardAmount + " name:" + rewardName);
                        if (ycGameAdCallback != null) {
                            ycGameAdCallback.gameAdSuccess();
                            ycGameAdCallback = null;
                        } else {
                            TakeGoldInfo takeGoldInfo = new TakeGoldInfo();
                            takeGoldInfo.setTaskId("3");
                            takeGoldInfo.setUserId(App.mUserInfo != null ? App.mUserInfo.getId() : "");
                            takeGoldInfo.setGold(50);
                            takeGoldInfo.setIsDouble(0);
                            takeGoldInfoPresenterImp.takeLuckGold(takeGoldInfo);
                        }
                    }

                    @Override
                    public void onSkippedVideo() {
                        Logger.i("rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            Logger.i("下载中，点击下载区域暂停");
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        Logger.i("下载暂停，点击下载区域继续");
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        Logger.i("下载失败，点击下载区域重新下载");
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        Logger.i("下载完成，点击下载区域重新下载");
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        Logger.i("安装完成，点击下载区域打开");
                    }
                });
            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void loadDataSuccess(TakeGoldInfoRet tData) {
        if (tData != null && tData.getCode() == Constants.SUCCESS) {
            Logger.i("take gold info--->" + JSON.toJSONString(tData));
            ToastUtils.showLong("领取成功");
        }
    }

    @Override
    public void loadDataError(Throwable throwable) {

    }
}