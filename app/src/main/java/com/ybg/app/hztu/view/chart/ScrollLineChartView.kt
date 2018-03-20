package com.ybg.app.hztu.view.chart

import android.content.Context
import android.graphics.*
import android.support.v4.util.Pair
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import com.ybg.app.base.bean.LineChartItem
import com.ybg.app.hztu.R
import java.util.*

/**
 * Created by yangbagang on 2018/3/20.
 */
class ScrollLineChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    private val backgroundColor: Int
    private var minViewHeight: Int = 0 //控件的最低高度
    private val minPointHeight: Int//折线最低点的高度
    private val lineInterval: Int //折线线段长度
    private var pointRadius: Float = 0.toFloat() //折线点的半径
    private var textSize: Float = 0.toFloat() //字体大小
    private var pointGap: Float = 0.toFloat() //折线单位高度差
    private var defaultPadding: Int = 0 //折线坐标图四周留出来的偏移量
    private var iconWidth: Float = 0.toFloat()  //天气图标的边长
    private var viewHeight: Int = 0
    private var viewWidth: Int = 0
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0

    private var linePaint: Paint? = null //线画笔
    private var textPaint: Paint? = null //文字画笔
    private var circlePaint: Paint? = null //圆点画笔

    /**
     * 公开方法，用于设置元数据
     *
     * @param data
     */
    var data: List<LineChartItem>? = ArrayList()
        set(data) {
            if (data == null) {
                return
            }
            field = data
            notifyDataSetChanged()
        } //元数据
    private val weatherDatas = ArrayList<Pair<Int, String>>()  //对元数据中天气分组后的集合
    private val points = ArrayList<PointF>() //折线拐点的集合
    private var maxTemperature = 0f//元数据中的最高和最低温度
    private var minTemperature = 0f

    private var velocityTracker: VelocityTracker? = null
    private val scroller: Scroller
    private val viewConfiguration: ViewConfiguration

    private var lastX = 0f

    init {
        scroller = Scroller(context)
        viewConfiguration = ViewConfiguration.get(context)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollLineChartView)
        minPointHeight = ta.getDimension(R.styleable.ScrollLineChartView_min_point_height, dp2pxF(context, 60f)).toInt()
        lineInterval = ta.getDimension(R.styleable.ScrollLineChartView_line_interval, dp2pxF(context, 60f)).toInt()
        backgroundColor = ta.getColor(R.styleable.ScrollLineChartView_background_color, Color.WHITE)
        ta.recycle()

        setBackgroundColor(backgroundColor)

        initSize(context)

        initPaint(context)
    }

    /**
     * 初始化默认数据
     */
    private fun initSize(c: Context) {
        screenWidth = resources.displayMetrics.widthPixels
        screenHeight = resources.displayMetrics.heightPixels

        minViewHeight = 3 * minPointHeight  //默认3倍
        pointRadius = dp2pxF(c, 2.5f)
        textSize = sp2pxF(c, 10f)
        defaultPadding = (0.5 * minPointHeight).toInt()  //默认0.5倍
        iconWidth = 1.0f / 3.0f * lineInterval //默认1/3倍
    }

    /**
     * 计算折线单位高度差
     */
    private fun calculatePointGap() {
        var lastMaxTem: Float = 0f
        var lastMinTem: Float = 0f
        for ((_, yValue) in this.data!!) {
            if (yValue > lastMaxTem) {
                maxTemperature = yValue
                lastMaxTem = yValue
            }
            if (yValue < lastMinTem) {
                minTemperature = yValue
                lastMinTem = yValue
            }
        }
        var gap = (maxTemperature - minTemperature) * 1.0f
        gap = if (gap == 0.0f) 1.0f else gap  //保证分母不为0
        pointGap = (viewHeight - minPointHeight - 2 * defaultPadding) / gap
    }

    private fun initPaint(c: Context) {
        linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        linePaint!!.strokeWidth = dp2px(c, 1f).toFloat()

        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint!!.textSize = textSize
        textPaint!!.color = Color.BLACK
        textPaint!!.textAlign = Paint.Align.CENTER

        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint!!.strokeWidth = dp2pxF(c, 1f)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initSize(context)
        calculatePointGap()
    }

    fun notifyDataSetChanged() {
        if (this.data == null) {
            return
        }
        weatherDatas.clear()
        points.clear()

        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        if (heightMode == View.MeasureSpec.EXACTLY) {
            viewHeight = Math.max(heightSize, minViewHeight)
        } else {
            viewHeight = minViewHeight
        }

        var totalWidth = 0
        if (this.data!!.size > 1) {
            totalWidth = 2 * defaultPadding + lineInterval * (this.data!!.size - 1)
        }
        viewWidth = Math.max(screenWidth, totalWidth) + 60//默认控件最小宽度为屏幕宽度

        setMeasuredDimension(viewWidth, viewHeight)
        calculatePointGap()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (this.data!!.isEmpty()) {
            return
        }
        drawAxis(canvas)

        drawLinesAndPoints(canvas)

        drawTemperature(canvas)
    }

    /**
     * 画时间轴
     *
     * @param canvas
     */
    private fun drawAxis(canvas: Canvas) {
        canvas.save()
        linePaint!!.color = DEFAULT_GRAY
        linePaint!!.strokeWidth = dp2px(context, 1f).toFloat()

        canvas.drawLine(defaultPadding.toFloat(),
                (viewHeight - defaultPadding).toFloat(),
                (viewWidth - defaultPadding).toFloat(),
                (viewHeight - defaultPadding).toFloat(),
                linePaint!!)

        val centerY = viewHeight - defaultPadding + dp2pxF(context, 15f)
        var centerX: Float
        for (i in this.data!!.indices) {
            val text = this.data!![i].xValue
            centerX = (defaultPadding + i * lineInterval).toFloat()
            val m = textPaint!!.fontMetrics
            canvas.drawText(text, 0, text.length, centerX, centerY - (m.ascent + m.descent) / 2, textPaint!!)
        }
        canvas.restore()
    }

    /**
     * 画折线和它拐点的园
     *
     * @param canvas
     */
    private fun drawLinesAndPoints(canvas: Canvas) {
        canvas.save()
        linePaint!!.color = DEFAULT_BULE
        linePaint!!.strokeWidth = dp2pxF(context, 1f)
        linePaint!!.style = Paint.Style.STROKE

        val linePath = Path() //用于绘制折线
        points.clear()
        val baseHeight = defaultPadding + minPointHeight
        var centerX: Float
        var centerY: Float
        for (i in this.data!!.indices) {
            var tem = this.data!![i].yValue
            tem = tem - minTemperature
            centerY = (viewHeight - (baseHeight + tem * pointGap)).toInt().toFloat()
            centerX = (defaultPadding + i * lineInterval).toFloat()
            points.add(PointF(centerX, centerY))
            if (i == 0) {
                linePath.moveTo(centerX, centerY)
            } else {
                linePath.lineTo(centerX, centerY)
            }
        }
        canvas.drawPath(linePath, linePaint!!) //画出折线

        //接下来画折线拐点的园
        var x: Float
        var y: Float
        for (i in points.indices) {
            x = points[i].x
            y = points[i].y

            //先画一个颜色为背景颜色的实心园覆盖掉折线拐角
            circlePaint!!.style = Paint.Style.FILL_AND_STROKE
            circlePaint!!.color = backgroundColor
            canvas.drawCircle(x, y,
                    pointRadius + dp2pxF(context, 1f),
                    circlePaint!!)
            //再画出正常的空心园
            circlePaint!!.style = Paint.Style.STROKE
            circlePaint!!.color = DEFAULT_BULE
            canvas.drawCircle(x, y,
                    pointRadius,
                    circlePaint!!)
        }
        canvas.restore()
    }

    /**
     * 画温度描述值
     *
     * @param canvas
     */
    private fun drawTemperature(canvas: Canvas) {
        canvas.save()

        textPaint!!.textSize = 1.2f * textSize //字体放大一丢丢
        var centerX: Float
        var centerY: Float
        var text: String
        for (i in points.indices) {
            text = "" + this.data!![i].yValue
            centerX = points[i].x
            centerY = points[i].y - dp2pxF(context, 13f)
            val metrics = textPaint!!.fontMetrics
            canvas.drawText(text,
                    centerX,
                    centerY - (metrics.ascent + metrics.descent) / 2,
                    textPaint!!)
        }
        textPaint!!.textSize = textSize
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x = 0f
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain()
        }
        velocityTracker!!.addMovement(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!scroller.isFinished) {  //fling还没结束
                    scroller.abortAnimation()
                }
                x = event.x
                lastX = x
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                x = event.x
                val deltaX = (lastX - x).toInt()
                if (scrollX + deltaX < 0) {    //越界恢复
                    scrollTo(0, 0)
                    return true
                } else if (scrollX + deltaX > viewWidth - screenWidth) {
                    scrollTo(viewWidth - screenWidth, 0)
                    return true
                }
                scrollBy(deltaX, 0)
                lastX = x
            }
            MotionEvent.ACTION_UP -> {
                x = event.x
                velocityTracker!!.computeCurrentVelocity(1000)  //计算1秒内滑动过多少像素
                val xVelocity = velocityTracker!!.xVelocity.toInt()
                if (Math.abs(xVelocity) > viewConfiguration.scaledMinimumFlingVelocity) {  //滑动速度可被判定为抛动
                    scroller.fling(scrollX, 0, -xVelocity, 0, 0, viewWidth - screenWidth, 0, 0)
                    invalidate()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            invalidate()
        }
    }

    companion object {

        private val DEFAULT_BULE = -0xff4001
        private val DEFAULT_GRAY = Color.GRAY

        //工具类
        fun dp2px(c: Context, dp: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.resources.displayMetrics).toInt()
        }

        fun sp2px(c: Context, sp: Float): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, c.resources.displayMetrics).toInt()
        }

        fun dp2pxF(c: Context, dp: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.resources.displayMetrics)
        }

        fun sp2pxF(c: Context, sp: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, c.resources.displayMetrics)
        }
    }

}
