package com.ydys.moneywalk.ui.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.jaeger.library.StatusBarUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.ydys.moneywalk.App;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.common.Constants;
import com.ydys.moneywalk.presenter.Presenter;
import com.ydys.moneywalk.ui.custom.ActRuleDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import pl.droidsonroids.gif.GifImageView;

public class InviteFriendActivity extends BaseActivity {

    @BindView(R.id.wx_invite)
    GifImageView mWxInviteGif;

    @BindView(R.id.tv_invite_code)
    TextView mInviteCodeTv;

    @BindView(R.id.tv_copy)
    TextView mCopyTv;

    ActRuleDialog actRuleDialog;

    private Bitmap waterHeadBitmap;

    private ShareAction shareAction;

    private ProgressDialog progressDialog = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_friend;
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
        FileDownloader.setup(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在分享");
    }

    @Override
    protected void initViews() {
        actRuleDialog = new ActRuleDialog(this, R.style.common_dialog);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mInviteCodeTv.setText(App.mUserInfo != null ? App.mUserInfo.getId() : "");
    }

    @OnClick(R.id.layout_act_rule)
    void actRule() {
        if (actRuleDialog != null && !actRuleDialog.isShowing()) {
            actRuleDialog.show();
        }
    }

    @OnClick(R.id.tv_copy)
    void copyInviteCode() {
        copyTextToSystem(this, App.mUserInfo != null ? App.mUserInfo.getId() : "");
        ToastUtils.showLong("已复制");
    }

    /**
     * 复制文本到系统剪切板
     */
    public static void copyTextToSystem(Context context, String text) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", text);
        cm.setPrimaryClip(mClipData);
    }

    BaseDownloadTask task;

    @OnClick(R.id.wx_invite)
    void wxInvite() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }

        final String sharePath = PathUtils.getExternalAppFilesPath() + "/share_bg.png";
        String imgUrl = Constants.BASE_IMAGE_URL + App.initInfo.getAppConfig().getSharePic();
        Logger.i("share path --->" + sharePath + "---img url --->" + imgUrl);

        Glide.with(this).asBitmap().load(App.mUserInfo != null ? App.mUserInfo.getFace() : "").into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                waterHeadBitmap = resource;

                task = FileDownloader.getImpl().create(imgUrl)
                        .setPath(sharePath)
                        .setListener(new FileDownloadListener() {
                            @Override
                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            }

                            @Override
                            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                            }

                            @Override
                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            }

                            @Override
                            protected void blockComplete(BaseDownloadTask task) {
                            }

                            @Override
                            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                            }

                            @Override
                            protected void completed(BaseDownloadTask task) {
                                Logger.i("down finish --->" + sharePath);
                                mergeImage(sharePath);
                            }

                            @Override
                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                            }

                            @Override
                            protected void error(BaseDownloadTask task, Throwable e) {
                            }

                            @Override
                            protected void warn(BaseDownloadTask task) {
                            }
                        });

                task.start();
            }
        });

    }

    public void mergeImage(String srcPath) {
        int[][] XYPoints = {{440, 145}, {864, 711}, {864, 1070}, {864, 1469}};

        //背景图
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath);
        //测试头像
        //Bitmap waterMap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_head);

        Bitmap roundHeadBitmap = getOvalBitmap(ImageUtils.scale(waterHeadBitmap, 217, 217));
        Bitmap normalBitmap = getRoundedCornerBitmap(ImageUtils.scale(waterHeadBitmap, 127, 127), 10);

        Bitmap last = addImageWatermark(bitmap, roundHeadBitmap, normalBitmap, XYPoints, 255, true);
        final String resPath = PathUtils.getExternalAppFilesPath() + "/share_res_bg.png";


        String content = App.mUserInfo != null ? "我的邀请码：" + App.mUserInfo.getId() : "";
        int color = ContextCompat.getColor(this, R.color.white);
        int txSize = SizeUtils.dp2px(18);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setTextSize(txSize);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        int txWidth = (int) paint.measureText(content);

        int x = (bitmap.getWidth() - txWidth) / 2;
        Bitmap resultBitmap = ImageUtils.addTextWatermark(last, content, txSize, color, x, 515);
        ImageUtils.save(resultBitmap, resPath, Bitmap.CompressFormat.PNG);
        Logger.i("share path--->" + resPath);

        if (resultBitmap != null) {
            if (shareAction == null) {
                shareAction = new ShareAction(this);
                shareAction.setCallback(shareListener);//回调监听器
            }

            UMImage image = new UMImage(this, resultBitmap);//bitmap文件
            UMImage thumb = new UMImage(this, R.mipmap.logo1);
            image.setThumb(thumb);
            shareAction.withMedia(image);
            shareAction.setPlatform(SHARE_MEDIA.WEIXIN).share();
        } else {
            ToastUtils.showLong("分享失败");
        }
    }

    public Bitmap addImageWatermark(final Bitmap src,
                                    final Bitmap roundBitMap,
                                    final Bitmap watermark,
                                    int[][] points,
                                    final int alpha,
                                    final boolean recycle) {
        Bitmap ret = src.copy(src.getConfig(), true);
        if (!isEmptyBitmap(watermark)) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Canvas canvas = new Canvas(ret);
            paint.setAlpha(alpha);

            for (int i = 0; i < points.length; i++) {
                if (i == 0) {
                    canvas.drawBitmap(roundBitMap, points[i][0], points[i][1], paint);
                } else {
                    canvas.drawBitmap(watermark, points[i][0], points[i][1], paint);
                }
            }
        }
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    //获取圆形图
    public static Bitmap getOvalBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public void dismissShareView() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            dismissShareView();
            Toast.makeText(InviteFriendActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Logger.i(t.getMessage());
            dismissShareView();
            Toast.makeText(InviteFriendActivity.this, "分享失败", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            dismissShareView();
            Toast.makeText(InviteFriendActivity.this, "取消分享", Toast.LENGTH_LONG).show();
        }
    };

    @OnClick(R.id.iv_back)
    void back() {
        finish();
    }
}
