package com.joyrun.bannersample

import android.content.Context
import android.util.AttributeSet
import cn.jzvd.JzvdStd

/**
 * author: wenjie
 * date: 2022/9/23 14:11
 * description:
 */
class AdvertVideoView : JzvdStd {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributes: AttributeSet? = null) : super(context, attributes)

    override fun onStateAutoComplete() {
        super.onStateAutoComplete()
        startVideo()
    }

//    override fun getLayoutId(): Int {
//        return R.layout.advert_video_view
//    }
}