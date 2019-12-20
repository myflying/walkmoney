package com.ydys.elsbballs.ui.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.blankj.utilcode.util.SizeUtils;
import com.ydys.elsbballs.R;


public class StepNumProgressView1 extends View {
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
    private int mScaleHeight = dp2px(18);
    /**
     * 刻度高 长针
     */
    private int mScaleHeight1 = dp2px(26);
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
     * 当前步数
     */
    private int mTemperature = 15;
    //
    /**
     * 最低步数
     */
    private int mMinTemp = 0;
    /**
     * 最高步数
     */
    private int mMaxTemp = 60;

    /**
     * 间隔
     */
    private int mSpace = 15;

    /**
     * 步数每份的角度
     */
    private float mAngleOneTem = (float) 270 / (mMaxTemp - mMinTemp);
    /**
     * 步数，刻度的半径
     */
    private int mTemDialRadius;

    /**
     * 刻度和文字颜色
     */
    private String mTextColor = "#ffdbc2";
    /**
     * 未达到的步数
     */
    private String mDialBackGroundColor = "#ffdbc2";
    /**
     * 已经达到的步数
     */
    private String mDialForegroundColor = "#fd7212";

    public StepNumProgressView1(Context context) {
        super(context);
        init();
    }

    public StepNumProgressView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StepNumProgressView1(Context context, AttributeSet attrs, int defStyleAttr) {
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
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
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
        mExchangePaint.setColor(Color.parseColor(mTextColor));
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
        mTemDialRadius = mWidth / 2 - dp2px(30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画步数刻度盘和内的刻度
        drawTempDial(canvas);
        drawStepNumText(canvas, "899");
        //画"步"标题文字
        drawTemText(canvas);
        drawExChangeText(canvas);
        //绘制walklogo
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

        for (int i = mMinTemp; i <= mMaxTemp; i++) {
            if (i <= mTemperature) {
                mDialPaint.setColor(Color.parseColor(mDialForegroundColor));
//                if (i < 20) {
//                    LinearGradient linearGradient = new LinearGradient(0, -mTemDialRadius, 0, -mTemDialRadius - mScaleHeight,
//                            new int[]{
//                                    getResources().getColor(R.color.dline1_color),
//                                    getResources().getColor(R.color.dline1_color),
//                            },
//                            new float[]{0.2f, 0.7f}, Shader.TileMode.CLAMP);
//                    mDialPaint.setShader(linearGradient);
//                } else if (i < 30) {
//                    LinearGradient linearGradient = new LinearGradient(0, -mTemDialRadius, 0, -mTemDialRadius - mScaleHeight,
//                            new int[]{
//                                    getResources().getColor(R.color.dline2_color),
//                                    getResources().getColor(R.color.dline2_color),
//                            },
//                            new float[]{0.2f, 0.7f}, Shader.TileMode.CLAMP);
//                    mDialPaint.setShader(linearGradient);
//                } else if (i < 40) {
//                    LinearGradient linearGradient = new LinearGradient(0, -mTemDialRadius, 0, -mTemDialRadius - mScaleHeight,
//                            new int[]{
//                                    getResources().getColor(R.color.dline3_color),
//                                    getResources().getColor(R.color.dline3_color),
//                            },
//                            new float[]{0.2f, 0.7f}, Shader.TileMode.CLAMP);
//                    mDialPaint.setShader(linearGradient);
//                } else if (i < 50) {
//                    LinearGradient linearGradient = new LinearGradient(0, -mTemDialRadius, 0, -mTemDialRadius - mScaleHeight,
//                            new int[]{
//                                    getResources().getColor(R.color.dline4_color),
//                                    getResources().getColor(R.color.dline4_color),
//                            },
//                            new float[]{0.2f, 0.7f}, Shader.TileMode.CLAMP);
//                    mDialPaint.setShader(linearGradient);
//                } else if (i < 60) {
//                    LinearGradient linearGradient = new LinearGradient(0, -mTemDialRadius, 0, -mTemDialRadius - mScaleHeight,
//                            new int[]{
//                                    getResources().getColor(R.color.dline5_color),
//                                    getResources().getColor(R.color.dline5_color),
//                            },
//                            new float[]{0.2f, 0.7f}, Shader.TileMode.CLAMP);
//                    mDialPaint.setShader(linearGradient);
//                }
            } else {
                mDialPaint.setShader(null);
                mDialPaint.setColor(Color.parseColor(mDialBackGroundColor));
            }
            if (i % mSpace == 0) {
                //从刻度的内圈开始，往外画
                canvas.drawLine(0, -mTemDialRadius, 0, -mTemDialRadius - mScaleHeight1, mDialPaint);
                float tempWidth = mDialPaint.measureText((i * 100) + "");
                canvas.drawText((i * 100) + "", 0 - tempWidth / 2 - SizeUtils.dp2px(10), -mTemDialRadius + dp2px(20), mTitlePaint);
            } else {
                canvas.drawLine(0, -mTemDialRadius, 0, -mTemDialRadius - mScaleHeight, mDialPaint);
            }
            canvas.rotate(mAngleOneTem);
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
        canvas.drawText(num, -tempWidth / 2 - dp2px(10), mTemDialRadius / 4, mStepNumPaint);
        canvas.restore();
    }

    /**
     * 画标题"步"
     *
     * @param canvas
     */
    private void drawTemText(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        float tempWidth = mTitlePaint.measureText(mTempTitle);
        float stepWidth = mStepNumPaint.measureText("899");
        canvas.drawText(mTempTitle, stepWidth / 2 + 4, mTemDialRadius / 4 - 4, mTitlePaint);
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
        canvas.drawBitmap(bitmap, -bitmap.getWidth() / 2, -dp2px(72), mWalkPaint);
        canvas.restore();
    }

    public void setMinTemp(int minTemp) {
        setData(minTemp, mMaxTemp, mTemperature);
    }

    public void setMaxTemp(int maxTemp) {
        setData(mMinTemp, maxTemp, mTemperature);
    }

    public void setTemp(int temp) {
        setData(mMinTemp, mMaxTemp, temp);
    }

    private void setTempTitle(String tempTitle) {
        this.mTempTitle = tempTitle;
        invalidate();
    }

    /**
     * @param minTemp 最小步数
     * @param maxTemp 最大步数
     * @param temp    设置的步数
     */
    public void setData(int minTemp, int maxTemp, int temp) {
        this.mMinTemp = minTemp;
        this.mMaxTemp = maxTemp;
        if (temp < minTemp) {
            this.mTemperature = minTemp;
        } else if (temp > maxTemp) {
            this.mTemperature = maxTemp;
        } else {
            this.mTemperature = temp;
        }

        mAngleOneTem = (float) 270 / (mMaxTemp - mMinTemp);

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
