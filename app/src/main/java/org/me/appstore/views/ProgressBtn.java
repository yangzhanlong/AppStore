package org.me.appstore.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 创建者     itheima
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class ProgressBtn extends Button {
    private boolean isProgressEnable = true;
    private long mMax = 100;
    private long mProgress;
    private ColorDrawable mBlueBg;

    /***
     * 是否允许进度
     *
     * @param isProgressEnable
     */
    public void setIsProgressEnable(boolean isProgressEnable) {
        this.isProgressEnable = isProgressEnable;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public void setMax(long max) {
        mMax = max;
    }

    /**
     * 设置进度的当前值
     *
     * @param progress
     */
    public void setProgress(long progress) {
        mProgress = progress;
        //重绘进度
        invalidate();
    }

    public ProgressBtn(Context context) {
        super(context);
    }

    public ProgressBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //之前
//        canvas.drawText("haha", 20, 20, getPaint());
        if (isProgressEnable) {
            if (mBlueBg == null) {
                mBlueBg = new ColorDrawable(Color.BLUE);
            }
            int left = 0;
            int top = 0;
            int right = (int) (mProgress * 1.0f / mMax * getMeasuredWidth() + .5f);//动态计算
            int bottom = getBottom();
            mBlueBg.setBounds(left, top, right, bottom);
            mBlueBg.draw(canvas);
        }
        super.onDraw(canvas);//默认绘制

        //之后
    }


}
