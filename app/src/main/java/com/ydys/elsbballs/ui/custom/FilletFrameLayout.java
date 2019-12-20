package com.ydys.elsbballs.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FilletFrameLayout extends FrameLayout {
    public FilletFrameLayout(@NonNull Context context) {
        super(context);
    }

    public FilletFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FilletFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Path path = new Path();

        if(Build.VERSION.SDK_INT >= 26){
            canvas.clipRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()));
        }else {
            canvas.clipRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), Region.Op.UNION);
        }

        //path.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), SizeUtils.dp2px(4), SizeUtils.dp2px(4), Path.Direction.CW);
        canvas.clipPath(path, Region.Op.REPLACE);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        super.dispatchDraw(canvas);
    }
}
