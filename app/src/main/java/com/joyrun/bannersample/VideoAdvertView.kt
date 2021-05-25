package com.joyrun.bannersample

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

/**
 * author: wenjie
 * date: 2021-05-11 16:24
 * descption:
 */
class VideoAdvertView @JvmOverloads constructor(context: Context, attr: AttributeSet? = null, def: Int = 0) : VideoView(context, attr, def) {

    init {
        setZOrderOnTop(true)
//        setZOrderMediaOverlay(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getDefaultSize(0, widthMeasureSpec)
        val height = getDefaultSize(0, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

}