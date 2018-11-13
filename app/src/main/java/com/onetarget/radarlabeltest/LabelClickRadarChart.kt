package com.onetarget.radarlabeltest

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils

/**
 * Created by joker on 2018/11/2.
 */
class LabelClickRadarChart : RadarChart {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private var mOnLabelClickedListener: OnLabelClickListener? = null
    private var mDrawTextRectBuffer = Rect()
    private var mFontMetricsBuffer = Paint.FontMetrics()
    private var tempX = 0f
    private var tempY = 0f

    override fun init() {
        super.init()

        setOnTouchListener { _, event ->
            val pointBeans = computePosition(this)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    tempX = event.x
                    tempY = event.y
                }
                MotionEvent.ACTION_UP ->
                    mOnLabelClickedListener?.run {
                        for (i in pointBeans.indices) {
                            val pointBean = pointBeans[i]
                            if (pointBean.isIn(tempX, tempY)) {
                                onLabelClick(i)
                                return@setOnTouchListener true
                            }
                        }
                    }

                else -> {
                }
            }
            return@setOnTouchListener false
        }
    }

    interface OnLabelClickListener {
        fun onLabelClick(xAxisIndex: Int)
    }

    private fun computeStartPoint(text: String, x: Float, y: Float,
                                  paint: Paint,
                                  anchor: MPPointF, angleDegrees: Float): RadarPointBean {
        mDrawTextRectBuffer = Rect()
        mFontMetricsBuffer = Paint.FontMetrics()
        var drawOffsetX = 0f
        var drawOffsetY = 0f
        val lineHeight = paint.getFontMetrics(mFontMetricsBuffer)
        paint.getTextBounds(text, 0, text.length, mDrawTextRectBuffer)

        drawOffsetX -= mDrawTextRectBuffer.left.toFloat()

        drawOffsetY += -mFontMetricsBuffer.ascent

        if (angleDegrees != 0f) {
            drawOffsetX -= mDrawTextRectBuffer.width() * 0.5f
            drawOffsetY -= lineHeight * 0.5f
        } else {
            if (anchor.x != 0f || anchor.y != 0f) {
                drawOffsetX -= mDrawTextRectBuffer.width() * anchor.x
                drawOffsetY -= lineHeight * anchor.y
            }
            drawOffsetX += x
            drawOffsetY += y
        }
        return RadarPointBean(drawOffsetX, drawOffsetY, mDrawTextRectBuffer)
    }

    /**
     * 计算位置
     * @param compositeRadar
     * @return
     */
    private fun computePosition(compositeRadar: RadarChart): List<RadarPointBean> {
        val pointBeans = ArrayList<RadarPointBean>()
        val xAxis = compositeRadar.xAxis
        val labelRotationAngleDegrees = xAxis.labelRotationAngle
        val drawLabelAnchor = MPPointF.getInstance(0.5f, 0.25f)
        val sliceAngle = compositeRadar.sliceAngle
        val factor = compositeRadar.factor
        val center = compositeRadar.centerOffsets
        val pOut = MPPointF.getInstance(0f, 0f)
        for (i in 0 until compositeRadar.data.maxEntryCountSet.entryCount) {
            val label = xAxis.valueFormatter.getFormattedValue(i.toFloat(), xAxis)

            val angle = (sliceAngle * i + compositeRadar.rotationAngle) % 360f

            Utils.getPosition(center, compositeRadar.yRange * factor + xAxis.mLabelRotatedWidth / 2f, angle, pOut)

            pointBeans.add(computeStartPoint(label, pOut.x, pOut.y - xAxis.mLabelRotatedHeight / 2f,
                    mXAxisRenderer.paintAxisLabels, drawLabelAnchor, labelRotationAngleDegrees))
        }
        return pointBeans
    }

    fun setOnLabelClickListener(listener: OnLabelClickListener) {
        mOnLabelClickedListener = listener
    }

}