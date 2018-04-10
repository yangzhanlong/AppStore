package org.me.appstore.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by user on 2018/4/10.
 * 自定义环形进度条
 */

public class ProgressBar extends View {

    /**
     * 空心线宽
     */
    private float paintStrokeWidth;

    /**
     * 环形颜色
     */
    private Paint grayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  // 消除锯齿
    /**
     * 进度颜色
     */
    private Paint redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 字体颜色
     */
    private Paint bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 圆心
     */
    PointF center = new PointF();

    /**
     * 环形半径
     */
    private float radius;

    /**
     * 弧的外切矩形
     */
    RectF arcRectF = new RectF();

    /**
     * 下载百分比
     */
    private float percent = 0;

    /**
     * 环形内的文本
     */
    private String text;

    public ProgressBar(Context context) {
        this(context, null);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        // TypedValue.applyDimension()方法的功能就是把非标准尺寸转换成标准尺寸
        // COMPLEX_UNIT_DIP  dp->px
        // COMPLEX_UNIT_IN   in->px
        // COMPLEX_UNIT_MM   mm->px
        // COMPLEX_UNIT_PT   pt->px
        // COMPLEX_UNIT_SP   sp->px
        paintStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        grayPaint.setColor(Color.GRAY); // 灰色
        grayPaint.setStyle(Paint.Style.STROKE); // 空心
        grayPaint.setStrokeWidth(paintStrokeWidth); // 空心线宽

        redPaint.setColor(Color.RED);
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setStrokeWidth(paintStrokeWidth);

        bluePaint.setColor(Color.BLUE);
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
        bluePaint.setTextSize(textSize);
    }

    /**
     * 设置文本
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    /**
     * 设置百分比
     * @param percent
     */
    public void setPercent(float percent) {
        this.percent = percent;
        text = String.format("%.1f%%", percent * 100);
        invalidate();  // 重绘
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getCircleAttr();
        drawGrayRing(canvas);
        drawRedArc(canvas);
        drawBlueText(canvas);
    }

    /**
     * 画环形内文本
     * @param canvas
     */
    private void drawBlueText(Canvas canvas) {
        Rect textBounds = new Rect();
        // 计算要显示的字符串所占的宽度和高度
        bluePaint.getTextBounds(text, 0, text.length(), textBounds);
        // 参数一 ：绘制文本
        // 参数二： 原点坐标x (文本左下角)
        // 参数三: 原点坐标y (文本左下角)
        // 参数四：画笔
        canvas.drawText(text, center.x - textBounds.width() / 2, center.y + textBounds.height() / 2, bluePaint);
    }

    /**
     * 画进度(弧)
     * @param canvas
     */
    private void drawRedArc(Canvas canvas) {
        /**
         * 360度进制， 0度是3点钟方向， 顺时针是正的
         * @param oval          弧的外切矩形
         * @param startAngle    弧的起始角度
         * @param sweepAngle    弧扫过的角度
         * @param useCenter     如果是true就会把弧的两端与圆心相连，如果是false只画弧
         * @param paint         画笔
         */
        canvas.drawArc(arcRectF, -90, percent * 360, false, redPaint);
    }

    /**
     * 画环形
     * @param canvas
     */
    private void drawGrayRing(Canvas canvas) {
        canvas.drawCircle(center.x, center.y, radius, grayPaint);
    }

    /**
     * 获取环形属性  圆心  半径  外接矩形
     */
    public void getCircleAttr() {
        center.x = getWidth() / 2;
        center.y = getHeight() / 2;
        radius = Math.min(getWidth(), getHeight()) / 2 - paintStrokeWidth / 2;

        // 环形的外接矩形
        arcRectF.set(center.x - radius, center.y - radius, center.x + radius, center.y + radius);
    }
}
