package com.ydys.moneywalk.ui.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.orhanobut.logger.Logger;
import com.ydys.moneywalk.R;
import com.ydys.moneywalk.common.Constants;


public class StepNumProgressView extends View {
    /**
     * 控件宽
     */
    private int mWidth;
    /**
     * 控件高
     */
    private int mHeight;

    private String mTempTitle = "步";

    private String mExChangeTitle = "满1步领取10金币";
    /**
     * 刻度高 短针
     */
    private int mScaleHeight = dp2px(15);
    /**
     * 刻度高 长针
     */
    private int mScaleHeight1 = dp2px(20);
    /**
     * 刻度盘/
     */
    private Paint mDialPaint;
    /**
     * 文本画笔
     */
    private Paint mTitlePaint;

    /**
     * 步数画笔
     */
    private Paint mStepNumPaint;

    private Paint mWalkPaint;

    //提示语
    private Paint mExchangePaint;

    /**
     * 当前进度
     */
    private int mCurrentProgress;
    //
    /**
     * 最低步数
     */
    private int mMinStep;
    /**
     * 最高步数
     */
    private int mMaxStep = 60;

    /**
     * 间隔
     */
    private int mSpace = 15;

    /**
     * 步数每份的角度
     */
    private float mAngleOneTem = (float) 270 / (mMaxStep - mMinStep);
    /**
     * 步数，刻度的半径
     */
    private int mTemDialRadius;

    /**
     * 刻度颜色
     */
    private String mTextColor = "#ffdbc2";

    /**
     * "步数值"颜色
     */
    private String mStepNumColor = "#333333";
    /**
     * "步"颜色
     */
    private String mExchangeColor = "#b1b1b1";
    /**
     * 未达到的步数
     */
    private String mDialBackGroundColor = "#ffdbc2";
    /**
     * 已经达到的步数
     */
    private String mDialForegroundColor = "#fd7212";

    private int walkNum;

    public StepNumProgressView(Context context) {
        super(context);
        init();
    }

    public StepNumProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StepNumProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //步数刻度盘
        mDialPaint = new Paint();
        mDialPaint.setAntiAlias(true);
        mDialPaint.setColor(Color.parseColor(mDialBackGroundColor));
        mDialPaint.setStrokeWidth(dp2px(3));
        mDialPaint.setStrokeCap(Paint.Cap.ROUND);
        mDialPaint.setStyle(Paint.Style.STROKE);

        //文字描述
        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setTextSize(sp2px(15));
        mTitlePaint.setColor(Color.parseColor(mTextColor));
        mTitlePaint.setStyle(Paint.Style.STROKE);

        mStepNumPaint = new Paint();
        Typeface font = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD);
        mStepNumPaint.setTypeface(font);
        mStepNumPaint.setAntiAlias(true);
        mStepNumPaint.setTextSize(sp2px(36));
        mStepNumPaint.setColor(Color.parseColor("#000000"));
        mStepNumPaint.setStyle(Paint.Style.STROKE);

        mWalkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mWalkPaint.setFilterBitmap(true);
        mWalkPaint.setDither(true);

        mExchangePaint = new Paint();
        mExchangePaint.setAntiAlias(true);
        mExchangePaint.setTextSize(sp2px(12));
        mExchangePaint.setColor(Color.parseColor(mExchangeColor));
        mExchangePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        // 参考宽，处理成正方形
        setMeasuredDimension(specSize, specSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 控件宽、高
        mWidth = mHeight = Math.min(h, w);
        // 步数，刻度的半径
        mTemDialRadius = mWidth / 2 - dp2px(24);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Logger.i("step draw--->");
        //画步数刻度盘和内部的刻度
        drawTempDial(canvas);
        drawTempDialValue(canvas);
        //绘制步数值
        drawStepNumText(canvas, walkNum + "");
        //画"步"标题文字
        drawTemText(canvas);
        //绘制兑换说明
        drawExChangeText(canvas);
        //绘制walk_logo
        drawWalkImage(canvas);
    }

    /**
     * 画步数刻度 和步数内圈文字
     */
    private void drawTempDial(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // 顺时针旋转135度
        canvas.rotate(-135);

        for (int i = mMinStep; i <= mMaxStep; i++) {
            if (i <= mCurrentProgress) {
                mDialPaint.setColor(Color.parseColor(mDialForegroundColor));
            } else {
                mDialPaint.setShader(null);
                mDialPaint.setColor(Color.parseColor(mDialBackGroundColor));
            }
            if (i % mSpace == 0) {
                //从刻度的内圈开始，往外画
                canvas.drawLine(0, -mTemDialRadius, 0, -mTemDialRadius - mScaleHeight1, mDialPaint);

//                Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
//                mTitlePaint.setTypeface(font);
//                mTitlePaint.setAntiAlias(true);
//                mTitlePaint.setTextSize(sp2px(16));
//                mTitlePaint.setColor(Color.parseColor(mTextColor));
//                mTitlePaint.setStyle(Paint.Style.STROKE);
//                float tempWidth = mDialPaint.measureText((i * 100) + "");
//
//                //对起始刻度处理
//                String tempText = i == 0 ? "1" : (i * 100) + "";
//                canvas.drawText(tempText, 0 - tempWidth / 2 - SizeUtils.dp2px(12), -mTemDialRadius + dp2px(20), mTitlePaint);
            } else {
                canvas.drawLine(0, -mTemDialRadius, 0, -mTemDialRadius - mScaleHeight, mDialPaint);
            }
            canvas.rotate(mAngleOneTem);
        }
        canvas.restore();
    }


    private void drawTempDialValue(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        for (int i = mMinStep; i <= mMaxStep; i++) {
            if (i % mSpace == 0) {

                Typeface font = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
                mTitlePaint.setTypeface(font);
                mTitlePaint.setAntiAlias(true);
                mTitlePaint.setTextSize(sp2px(16));
                mTitlePaint.setColor(Color.parseColor(mTextColor));
                mTitlePaint.setStyle(Paint.Style.STROKE);
                float tempWidth = mDialPaint.measureText((i * 100) + "");

                //对起始刻度处理
                String tempText = i == 0 ? "1" : (i * 100) + "";
                canvas.drawText(tempText, 0 - mTemDialRadius + tempWidth / 2 , mTemDialRadius - (SizeUtils.dp2px(20) + (i / mSpace) * mTemDialRadius / 2), mTitlePaint);
            }
        }
        canvas.restore();
    }

    /**
     * 画标题步数等值
     *
     * @param canvas
     */
    private void drawStepNumText(Canvas canvas, String num) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float tempWidth = mStepNumPaint.measureText(num);
        canvas.drawText(num, -tempWidth / 2 - dp2px(4), mTemDialRadius / 4, mStepNumPaint);
        canvas.restore();
    }

    /**
     * 画标题"步"
     *
     * @param canvas
     */
    private void drawTemText(Canvas canvas) {
        canvas.save();

        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setTextSize(sp2px(14));
        mTitlePaint.setColor(Color.parseColor(mStepNumColor));
        mTitlePaint.setStyle(Paint.Style.STROKE);

        canvas.translate(getWidth() / 2, getHeight() / 2);
        float stepWidth = mStepNumPaint.measureText(walkNum + "");
        canvas.drawText(mTempTitle, stepWidth / 2, mTemDialRadius / 4 - 4, mTitlePaint);
        canvas.restore();
    }

    /**
     * 画 "兑换提示语"
     *
     * @param canvas
     */
    private void drawExChangeText(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float tempWidth = mExchangePaint.measureText(mExChangeTitle);

        Paint.FontMetricsInt fontMetrics = mExchangePaint.getFontMetricsInt();
        float tempHeight = fontMetrics.bottom - fontMetrics.top;
        canvas.drawText(mExChangeTitle, -tempWidth / 2, mTemDialRadius / 4 + tempHeight + 10, mExchangePaint);
        canvas.restore();
    }

    private void drawWalkImage(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.mipmap.walk_logo);
        canvas.drawBitmap(bitmap, -bitmap.getWidth() / 2, -dp2px(60), mWalkPaint);
        canvas.restore();
    }

    public void setStepProgress(int progress) {
        setData(mMinStep, mMaxStep, progress);
    }

    public void setWalkStepNum(int walkNum) {
        this.walkNum = walkNum;
        invalidate();
    }

    public void updateStateTitle(int index) {
        this.mExChangeTitle = Constants.RECEIVE_TITLES[index];
        invalidate();
    }

    /**
     * @param minStepNum 最小步数
     * @param maxStepNum 最大步数
     * @param temp       设置的步数
     */
    public void setData(int minStepNum, int maxStepNum, int temp) {
        this.mMinStep = minStepNum;
        this.mMaxStep = maxStepNum;
        if (temp < minStepNum) {
            this.mCurrentProgress = minStepNum;
        } else if (temp > maxStepNum) {
            this.mCurrentProgress = maxStepNum;
        } else {
            this.mCurrentProgress = temp;
        }
        mAngleOneTem = (float) 270 / (mMaxStep - mMinStep);
        invalidate();
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }
}
