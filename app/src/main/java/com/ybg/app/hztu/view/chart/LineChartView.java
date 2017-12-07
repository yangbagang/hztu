package com.ybg.app.hztu.view.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbagang on 2017/12/6.
 */

public class LineChartView extends View {

    private int originX; // 原点x坐标

    private int originY; // 原点y坐标

    private int firstPointX; //第一个点x坐标

    private int firstMinX; // 移动时第一个点的最小x值

    private int firstMaxX; //移动时第一个点的最大x值

    private int intervalX = 130; // 坐标刻度的间隔

    private int intervalY = 80; // y轴刻度的间隔

    private List<String> xValues;

    private List<Float> yValues;

    private int mWidth; // 控件宽度

    private int mHeight; // 控件高度

    private int startX; // 滑动时上一次手指的x坐标值

    private int xyTextSize = 24; //xy轴文字大小

    private int paddingTop = 40;// 默认上下左右的padding

    private int paddingLeft = 160;

    private int paddingRight = 100;

    private int paddingDown = 50;

    private int scaleHeight = 10; // x轴刻度线高度

    private int textToXYAxisGap = 20; // xy轴的文字距xy线的距离

    private int leftRightExtra = intervalX / 3; //x轴左右向外延伸的长度

    private int labelCountY = 6; // Y轴刻度个数

    private int bigCircleR = 7;

    private int smallCircleR = 5;

    private float minValueY; // y轴最小值

    private float maxValueY = 0; // y轴最大值

    private Paint paintPrimary, paintBlue, paintRed, paintText;

    private GestureDetector gestureDetector;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        gestureDetector = new GestureDetector(context, new MyOnGestureListener());
    }

    private void initPaint() {
        paintPrimary = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintPrimary.setColor(Color.parseColor("#303F9F"));
        paintPrimary.setStyle(Paint.Style.STROKE);

        paintBlue = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBlue.setColor(Color.parseColor("#0198cd"));
        paintBlue.setStrokeWidth(3f);
        paintBlue.setStyle(Paint.Style.STROKE);

        paintRed = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintRed.setColor(Color.RED);
        paintRed.setStrokeWidth(3f);
        paintRed.setStyle(Paint.Style.STROKE);

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(xyTextSize);
        paintText.setStrokeWidth(2f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            mWidth = getWidth();
            mHeight = getHeight();

            originX = paddingLeft - leftRightExtra;
            originY = mHeight - paddingDown;

            firstPointX = paddingLeft;
            firstMinX = mWidth - originX - (xValues.size() - 1) * intervalX - leftRightExtra;
            // 滑动时，第一个点x值最大为paddingLeft，在大于这个值就不能滑动了
            firstMaxX = firstPointX;

            intervalY = (mHeight - paddingDown - paddingTop) / labelCountY;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawX(canvas);
        drawBrokenLine(canvas);
        drawY(canvas);
    }

    /**
     * 画x轴
     *
     * @param canvas
     */
    private void drawX(Canvas canvas) {
        Path path = new Path();
        path.moveTo(originX, originY);
        for (int i = 0; i < xValues.size(); i++) {
            // x轴线
            path.lineTo(mWidth - paddingRight, originY);  // 写死不变
            // x轴箭头
            canvas.drawLine(mWidth - paddingRight, originY, mWidth - paddingRight - 15, originY + 10, paintPrimary);
            canvas.drawLine(mWidth - paddingRight, originY, mWidth - paddingRight - 15, originY - 10, paintPrimary);

            // x轴线上的刻度线
            canvas.drawLine(firstPointX + i * intervalX, originY, firstPointX + i * intervalX, originY - scaleHeight, paintPrimary);
            // x轴上的文字
            canvas.drawText(xValues.get(i), firstPointX + i * intervalX - getTextWidth(paintText, "17.01") / 2,
                    originY + textToXYAxisGap + getTextHeight(paintText, "17.01"), paintText);
        }
        canvas.drawPath(path, paintPrimary);

        // x轴虚线
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.WHITE);

        Path path1 = new Path();
        DashPathEffect dash = new DashPathEffect(new float[]{8, 10, 8, 10}, 0);
        p.setPathEffect(dash);
        for (int i = 0; i < labelCountY; i++) {
            path1.moveTo(originX, mHeight - paddingDown - leftRightExtra - i * intervalY);
            path1.lineTo(mWidth - paddingRight, mHeight - paddingDown - leftRightExtra - i * intervalY);
        }
        canvas.drawPath(path1, p);
    }

    /**
     * 画折线
     *
     * @param canvas
     */
    private void drawBrokenLine(Canvas canvas) {
        canvas.save();
        if (xValues.size() == 0 || yValues.size() == 0) return;
        // y轴文字
        minValueY = yValues.get(0);
        for (int i = 0; i < yValues.size(); i++) {
            // 找出y轴的最大最小值
            if (yValues.get(i) > maxValueY) {
                maxValueY = yValues.get(i);
            }
            if (yValues.get(i) < minValueY) {
                minValueY = yValues.get(i);
            }
        }
        // 画折线
        float aver = (labelCountY - 1) * intervalY / (maxValueY - minValueY);
        Path path = new Path();
        for (int i = 0; i < yValues.size(); i++) {
            if (i == 0) {
                path.moveTo(firstPointX, mHeight - paddingDown - leftRightExtra - yValues.get(i) * aver + minValueY * aver);
            } else {
                path.lineTo(firstPointX + i * intervalX, mHeight - paddingDown - leftRightExtra - yValues.get(i) * aver + minValueY * aver);
            }
        }
        canvas.drawPath(path, paintBlue);
        // 折线中的圆点
        for (int i = 0; i < yValues.size(); i++) {
            canvas.drawCircle(firstPointX + i * intervalX,
                    mHeight - paddingDown - leftRightExtra - yValues.get(i) * aver + minValueY * aver, bigCircleR, paintBlue);
            canvas.drawCircle(firstPointX + i * intervalX,
                    mHeight - paddingDown - leftRightExtra - yValues.get(i) * aver + minValueY * aver, smallCircleR, paintRed);
        }
        canvas.restore();
    }

    /**
     * 画y轴
     *
     * @param canvas
     */
    private void drawY(Canvas canvas) {
        canvas.save();
        Path path = new Path();
        path.moveTo(originX, originY);

        for (int i = 0; i < labelCountY; i++) {
            // y轴线
            if (i == 0) {
                path.lineTo(originX, mHeight - paddingDown - leftRightExtra);
            } else {
                path.lineTo(originX, mHeight - paddingDown - leftRightExtra - i * intervalY);
            }

            int lastPointY = mHeight - paddingDown - leftRightExtra - i * intervalY;
            if (i == labelCountY - 1) {
                int lastY = lastPointY - leftRightExtra - leftRightExtra / 2;
                // y轴最后一个点后，需要额外加上一小段，就是一个半leftRightExtra的长度
                canvas.drawLine(originX, lastPointY, originX, lastY, paintPrimary);
                // y轴箭头
                canvas.drawLine(originX, lastY, originX - 10, lastY + 15, paintPrimary);
                canvas.drawLine(originX, lastY, originX + 10, lastY + 15, paintPrimary);
            }
        }
        canvas.drawPath(path, paintPrimary);

        // y轴文字
        float space = (maxValueY - minValueY) / (labelCountY - 1);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        List<String> yTitles = new ArrayList<>();
        for (int i = 0; i < labelCountY; i++) {
            yTitles.add(decimalFormat.format(minValueY + i * space));
        }
        for (int i = 0; i < yTitles.size(); i++) {
            canvas.drawText(yTitles.get(i), originX - textToXYAxisGap - getTextWidth(paintText, "00.00"),
                    mHeight - paddingDown - leftRightExtra - i * intervalY + getTextHeight(paintText, "00.00") / 2, paintText);
        }
        canvas.restore();
    }

    /**
     * 手势事件
     */
    class MyOnGestureListener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) { // 按下事件
            return false;
        }

        // 按下停留时间超过瞬时，并且按下时没有松开或拖动，就会执行此方法
        @Override
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) { // 单击抬起
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e1.getX() > originX && e1.getX() < mWidth - paddingRight &&
                    e1.getY() > paddingTop && e1.getY() < mHeight - paddingDown) {
                //注意：这里的distanceX是e1.getX()-e2.getX()
                distanceX = -distanceX;
                if (firstPointX + distanceX > firstMaxX) {
                    firstPointX = firstMaxX;
                } else if (firstPointX + distanceX < firstMinX) {
                    firstPointX = firstMinX;
                } else {
                    firstPointX = (int) (firstPointX + distanceX);
                }
                invalidate();
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
        } // 长按事件

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (yValues.size() < 7) {
            return false;
        }
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void setXValues(List<String> values) {
        this.xValues = values;
    }

    public void setYValues(List<Float> values) {
        this.yValues = values;
    }

    /**
     * 获取文字的宽度
     *
     * @param paint
     * @param text
     * @return
     */
    private int getTextWidth(Paint paint, String text) {
        return (int) paint.measureText(text);
    }

    /**
     * 获取文字的高度
     *
     * @param paint
     * @param text
     * @return
     */
    private int getTextHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

}
