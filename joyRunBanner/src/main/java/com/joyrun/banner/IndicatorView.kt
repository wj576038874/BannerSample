package com.joyrun.banner

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2

/**
 * author: wenjie
 * date: 2022/9/21 10:58
 * description:
 */
class IndicatorView : View, Indicator {

    private var indicatorBuildListener: (() -> Unit)? = null
    private var banner: JoyrunBanner? = null

    private var radius = 0
    private var indicatorPadding = 0
    private var gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM

    private var marginLeft = 0
    private var marginTop = 0
    private var marginRight = 0
    private var marginBottom = 0

    private var focusColor = Color.GRAY
    private var normalColor = Color.WHITE

    private var focusBitmap: Bitmap? = null
    private var normalBitmap: Bitmap? = null

    private var pageOffset = 0f
    private var defaultRadius = 0f

    private val mContext: Context
    private val paintStroke: Paint
    private val paintFill: Paint

    private var orientation = ViewPager2.ORIENTATION_HORIZONTAL

    private var itemCount = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(
        context, attrs, defStyleAttr
    ) {
        mContext = context
        paintStroke = Paint(Paint.ANTI_ALIAS_FLAG)
        paintStroke.style = Paint.Style.STROKE
        paintFill = Paint(Paint.ANTI_ALIAS_FLAG)
        paintFill.style = Paint.Style.FILL
        defaultRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 3f, resources.displayMetrics
        )
    }

    fun setBanner(banner: JoyrunBanner) {
        this.banner = banner
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val count = itemCount
        if (count <= 1) return
        val longSize: Int
        val shortSize: Int

        val longPaddingBefore: Int
        val longPaddingAfter: Int
        val shortPaddingBefore: Int
        val shortPaddingAfter: Int
        banner?.let { banner ->
            if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                longSize = banner.width
                shortSize = banner.height
                longPaddingBefore = paddingLeft + marginLeft
                longPaddingAfter = paddingRight + marginRight
                shortPaddingBefore = paddingTop + marginTop
                shortPaddingAfter = paintStroke.strokeWidth.toInt() + paddingBottom + marginBottom
            } else {
                longSize = banner.height
                shortSize = banner.width
                longPaddingBefore = paddingTop + marginTop
                longPaddingAfter = paintStroke.strokeWidth.toInt() + paddingBottom + marginBottom
                shortPaddingBefore = paddingLeft + marginLeft
                shortPaddingAfter = paddingRight + marginRight
            }

            val itemWidth: Float = getItemWidth()
            val widthRatio = if (isDrawResIndicator()) 1 else 2

            if (indicatorPadding == 0) {
                indicatorPadding = itemWidth.toInt()
            }

            var shortOffset = shortPaddingBefore.toFloat()
            var longOffset = longPaddingBefore.toFloat()

            val indicatorLength = (count - 1) * (itemWidth * widthRatio + indicatorPadding)
            val horizontalGravityMask = gravity and Gravity.HORIZONTAL_GRAVITY_MASK
            val verticalGravityMask = gravity and Gravity.VERTICAL_GRAVITY_MASK
            when (horizontalGravityMask) {
                Gravity.CENTER_HORIZONTAL -> {
                    longOffset =
                        (longSize - longPaddingBefore - longPaddingAfter - indicatorLength) / 2.0f
                }
                Gravity.RIGHT -> {
                    if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                        longOffset = longSize - longPaddingAfter - indicatorLength - itemWidth
                    }
                    if (orientation == ViewPager2.ORIENTATION_VERTICAL) {
                        shortOffset = shortSize - shortPaddingAfter - itemWidth
                    }
                }
                Gravity.LEFT -> {
                    longOffset += itemWidth
                }
            }

            when (verticalGravityMask) {
                Gravity.CENTER_VERTICAL -> {
                    shortOffset =
                        (shortSize - shortPaddingAfter - shortPaddingBefore - itemWidth) / 2
                }
                Gravity.BOTTOM -> {
                    if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                        shortOffset = shortSize - shortPaddingAfter - getItemHeight()
                    }
                    if (orientation == ViewPager2.ORIENTATION_VERTICAL) {
                        longOffset = longSize - longPaddingAfter - indicatorLength
                    }
                }
                Gravity.TOP -> {
                    shortOffset += itemWidth
                }
            }

            if (horizontalGravityMask == Gravity.CENTER_HORIZONTAL && verticalGravityMask == Gravity.CENTER_VERTICAL) {
                shortOffset = (shortSize - shortPaddingAfter - shortPaddingBefore - itemWidth) / 2
            }
            var dX: Float
            var dY: Float
            var pageFillRadius = radius.toFloat()
            if (paintStroke.strokeWidth > 0) {
                pageFillRadius -= paintStroke.strokeWidth / 2.0f
            }

            for (iLoop in 0 until count) {
                val drawLong = longOffset + iLoop * (itemWidth * widthRatio + indicatorPadding)
                if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    dX = drawLong
                    dY = shortOffset
                } else {
                    dX = shortOffset
                    dY = drawLong
                }
                if (isDrawResIndicator()) {
                    if (iLoop == currentItem) continue
                    normalBitmap?.let {
                        canvas.drawBitmap(it, dX, dY, paintFill)
                    }
                } else {
                    // Only paint fill if not completely transparent
                    if (paintFill.alpha > 0) {
                        paintFill.color = normalColor
                        canvas.drawCircle(dX, dY, pageFillRadius, paintFill)
                    }

                    // Only paint stroke if a stroke width was non-zero
                    if (pageFillRadius != radius.toFloat()) {
                        canvas.drawCircle(dX, dY, radius.toFloat(), paintStroke)
                    }
                }
            }

            //Draw the filled circle according to the current scroll
            val cx = currentItem * (itemWidth * widthRatio + indicatorPadding)
            if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                dX = longOffset + cx
                dY = shortOffset
            } else {
                dX = shortOffset
                dY = longOffset + cx
            }
            if (isDrawResIndicator()) {
                focusBitmap?.let {
                    canvas.drawBitmap(it, dX, dY, paintStroke)
                }
            } else {
                paintFill.color = focusColor
                canvas.drawCircle(dX, dY, radius.toFloat(), paintFill)
            }
        }
    }

    private fun isDrawResIndicator(): Boolean {
        return focusBitmap != null && normalBitmap != null
    }

    private fun getItemWidth(): Float {
        if (isDrawResIndicator()) {
            return focusBitmap?.width?.coerceAtLeast(normalBitmap?.width ?: 0)?.toFloat() ?: 0f
        }
        return if (radius == 0) defaultRadius else radius.toFloat()
    }

    private fun getItemHeight(): Float {
        if (isDrawResIndicator()) {
            return focusBitmap?.height?.coerceAtLeast(normalBitmap?.height ?: 0)?.toFloat() ?: 0f
        }
        return if (radius == 0) defaultRadius else radius.toFloat()
    }

    override fun initIndicator(pageSize: Int) {
        this.itemCount = pageSize
        requestLayout()
    }

    override fun setFocusColor(focusColor: Int): Indicator {
        this.focusColor = focusColor
        return this
    }

    override fun setNormalColor(normalColor: Int): Indicator {
        this.normalColor = normalColor
        return this
    }

    override fun setOrientation(orientation: Int): Indicator {
        this.orientation = orientation
        return this
    }

    override fun setRadius(radius: Int): Indicator {
        this.radius = radius
        return this
    }

    override fun setIndicatorPadding(indicatorPadding: Int): Indicator {
        this.indicatorPadding = indicatorPadding
        return this
    }

    override fun setStrokeColor(strokeColor: Int): Indicator {
        paintStroke.color = strokeColor
        return this
    }

    override fun setStrokeWidth(strokeWidth: Int): Indicator {
        paintStroke.strokeWidth = strokeWidth.toFloat()
        return this
    }

    override fun setGravity(gravity: Int): Indicator {
        this.gravity = gravity
        return this
    }

    override fun setFocusResId(focusResId: Int): Indicator {
        try {
            focusBitmap = BitmapFactory.decodeResource(resources, focusResId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    override fun setNormalResId(normalResId: Int): Indicator {
        try {
            normalBitmap = BitmapFactory.decodeResource(resources, normalResId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    override fun setFocusDrawableId(focusDrawableId: Int): Indicator {
        try {
            focusBitmap =
                BitmapUtil.drawableToBitmap(ContextCompat.getDrawable(mContext, focusDrawableId))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    override fun setNormalDrawableId(normalDrawableId: Int): Indicator {
        try {
            normalBitmap =
                BitmapUtil.drawableToBitmap(ContextCompat.getDrawable(mContext, normalDrawableId))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    override fun setFocusIcon(bitmap: Bitmap): Indicator {
        focusBitmap = bitmap
        return this
    }

    override fun setNormalIcon(bitmap: Bitmap): Indicator {
        this.normalBitmap = bitmap
        return this
    }

    override fun setMargin(left: Int, top: Int, right: Int, bottom: Int): Indicator {
        marginLeft = left
        marginTop = top
        marginRight = right
        marginBottom = bottom
        return this
    }

    override fun build() {
        indicatorBuildListener?.invoke()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        pageOffset = positionOffset
        invalidate()
    }

    private var currentItem = 0

    override fun onPageSelected(position: Int) {
        currentItem = position
        invalidate()
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    fun setIndicatorBuildListener(listener: () -> Unit) {
        indicatorBuildListener = listener
    }
}