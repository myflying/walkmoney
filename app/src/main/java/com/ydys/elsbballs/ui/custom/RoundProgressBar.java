package com.ydys.elsbballs.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.orhanobut.logger.Logger;
import com.ydys.elsbballs.R;

public class RoundProgressBar extends View {
    private float agy;
    private boolean agz;
    private int innerRoundColor;
    private int max;
    private Paint paint;
    private int progress;
    private int roundColor;
    private int style;
    private int textColor;
    private float textSize;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RoundProgressBar);
        this.roundColor = obtainStyledAttributes.getColor(R.styleable.RoundProgressBar_roundColor, ContextCompat.getColor(context, R.color.test));
        this.innerRoundColor = obtainStyledAttributes.getColor(R.styleable.RoundProgressBar_innerRoundColor,ContextCompat.getColor(context, R.color.white));
        this.agy = obtainStyledAttributes.getDimension(R.styleable.RoundProgressBar_roundWidth, 84f);
        this.textColor = obtainStyledAttributes.getColor(R.styleable.RoundProgressBar_textColor, ContextCompat.getColor(context, R.color.colorAccent));
        this.textSize = obtainStyledAttributes.getDimension(R.styleable.RoundProgressBar_textSize, 25);
        this.max = obtainStyledAttributes.getInteger(R.styleable.RoundProgressBar_max, 100);
        this.style = 0;
        this.agz = true;
        obtainStyledAttributes.recycle();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        int i = (int) (((float) width) - (this.agy / 2.0f));

        Logger.i("www" + width + "---i---" + i);

        this.paint = new Paint();
        this.paint.setColor(this.roundColor);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(this.agy);
        this.paint.setAntiAlias(true);
        canvas.drawCircle((float) width, (float) height, (float) i, this.paint);
        this.paint.setColor(this.innerRoundColor);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setAntiAlias(true);
        canvas.drawCircle((float) width, (float) height, ((float) i) - (this.agy / 2.0f), this.paint);
        this.paint.setStrokeWidth(this.agy);
        RectF rectF = new RectF((float) (width - i), (float) (width - i), (float) (width + i), (float) (i + width));
        Shader sweepGradient = new SweepGradient((float) width, (float) height, new int[]{getResources().getColor(R.color.dline1_color), getResources().getColor(R.color.blue), getResources().getColor(R.color.dline5_color)}, new float[]{0.0f, 0.4f, 0.76f});
        Matrix matrix = new Matrix();
        matrix.setRotate(134.0f, (float) width, (float) height);
        sweepGradient.setLocalMatrix(matrix);
        this.paint.setShader(sweepGradient);

        Logger.i("xxx--->" + (float) ((this.progress * 271) / this.max));

        switch (this.style) {
            case 0:
                this.paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(rectF, 134.0f, (float) ((this.progress * 271) / this.max), false, this.paint);
                break;
            case 1:
                this.paint.setStyle(Paint.Style.FILL);
                if (this.progress != 0) {
                    canvas.drawArc(rectF, 134.0f, (float) ((this.progress * 271) / this.max), true, this.paint);
                    break;
                }
                break;
        }
        if (this.agz) {
            this.paint.setStrokeWidth(0.0f);
            this.paint.setColor(this.textColor);
            this.paint.setTextSize(this.textSize);
            this.paint.setTypeface(Typeface.DEFAULT_BOLD);
            height = (int) ((((float) this.progress) / ((float) this.max)) * 100.0f);
            float measureText = this.paint.measureText(height + "%");
            if (this.style == 0 && height != 0) {
                canvas.drawText(height + "%", ((float) width) - (measureText / 2.0f), ((float) width) + (this.textSize / 2.0f), this.paint);
            }
        }
    }

    public Paint getPaint() {
        return this.paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getRoundColor() {
        return this.roundColor;
    }

    public void setRoundColor(int i) {
        this.roundColor = i;
    }

    public float getRoundWidth() {
        return this.agy;
    }

    public void setRoundWidth(float f) {
        this.agy = f;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int i) {
        this.textColor = i;
    }

    public float getTextSize() {
        return this.textSize;
    }

    public void setTextSize(float f) {
        this.textSize = f;
    }

    public synchronized int getMax() {
        return this.max;
    }

    public synchronized void setMax(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("max must more than 0");
        }
        this.max = i;
    }

    public synchronized int getProgress() {
        return this.progress;
    }

    public synchronized void setProgress(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("progress must more than 0");
        }
        if (i > this.max) {
            this.progress = i;
        }
        if (i <= this.max) {
            this.progress = i;
            postInvalidate();
        }
    }

    public void setDisplayText(boolean z) {
        this.agz = z;
    }

    public int getStyle() {
        return this.style;
    }

    public void setStyle(int i) {
        this.style = i;
    }
}
