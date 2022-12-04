package com.example.diagram

import android.content.ClipData
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View

class GraphView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var mShowText = true
    private var textPos = 0
    private var textColor = 0
    private var textHeight = 14f
    private var textWidth = 14f
    private var xCircleCenter = 0f
    private var yCircleCenter = 0f
    private var shadowRectF : RectF = RectF()
    private val data = mutableListOf<Int>()
    private var bounds = RectF()
    private val shadowBias = 10f

    fun isShowText(): Boolean {
        return mShowText
    }

    fun setShowText(showText: Boolean) {
        mShowText = showText
        invalidate()
        requestLayout()
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PieChart,
            0, 0).apply {
                mShowText = getBoolean(R.styleable.PieChart_showText, false)
                textPos = getInteger(R.styleable.PieChart_labelPosition, 0)
                xCircleCenter = (width.toFloat() - shadowBias) / 2
                yCircleCenter = (height.toFloat() - shadowBias) / 2
                bounds = RectF(shadowBias,shadowBias, width.toFloat() + shadowBias, height.toFloat() + shadowBias)
            try {
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Account for padding
        var xpad = (paddingLeft + paddingRight).toFloat()
        val ypad = (paddingTop + paddingBottom).toFloat()

// Account for the label
        if (mShowText) xpad += textWidth

        val ww = w.toFloat() - xpad
        val hh = h.toFloat() - ypad

// Figure out how big we can make the pie.
        val diameter = Math.min(ww, hh)

        xCircleCenter = (width.toFloat() - shadowBias) / 2
        yCircleCenter = (height.toFloat() - shadowBias) / 2
        bounds = RectF(shadowBias,shadowBias, width.toFloat(), height.toFloat())
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Try for a width based on our minimum
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = View.resolveSizeAndState(minw, widthMeasureSpec, 1)

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        val minh: Int = View.MeasureSpec.getSize(w) - textWidth.toInt() + paddingBottom + paddingTop
        val h: Int = View.resolveSizeAndState(minh, heightMeasureSpec, 0)

        setMeasuredDimension(w, h)
    }


    private val thisCirclePaint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }

    private val textPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = textColor
        if (textHeight == 0f) {
            textHeight = textSize
        } else {
            textSize = textHeight
        }
    }

    private val piePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = textHeight
    }

    private val shadowPaint = Paint(0).apply {
        color = Color.CYAN
        maskFilter = BlurMaskFilter(8f, BlurMaskFilter.Blur.NORMAL)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.apply {
            // Draw the shadow
           drawOval(bounds, shadowPaint)

            // Draw the label text
            //drawText(data[mCurrentItem].mLabel, textX, textY, textPaint)

            // Draw the pie slices
           /* data.forEach {
                piePaint.shader = it.mShader
                drawArc(bounds,
                    360 - it.endAngle,
                    it.endAngle - it.startAngle,
                    true, piePaint)
            }*/

            // Draw the pointer
            //drawLine(textX, pointerY, pointerX, pointerY, textPaint)
            drawCircle(xCircleCenter, yCircleCenter, xCircleCenter, thisCirclePaint)
        }
    }


}