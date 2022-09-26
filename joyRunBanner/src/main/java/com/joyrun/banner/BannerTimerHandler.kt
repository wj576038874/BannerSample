package com.joyrun.banner

import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * author: wenjie
 * date: 2022/9/2 10:42
 * description: 为做做曝光埋点，banner滑出屏幕外，或者切换到其他页面时，应该暂停轮播
 * 所以banner轮播定时器，将banner设置为不自动轮播
 * 自行写定时轮播器来控制，再生命周期函数中和滑动监听中判断banenr是否可见来暂停或者开启轮播
 */
class BannerTimerHandler @JvmOverloads constructor(private val timerHandler: TimerHandlerListener?, private val interval: Long = 3000){

    private var isStopped = true

    private val mHandler = Handler(Looper.getMainLooper())
    private val mRunnable = object : Runnable{
        override fun run() {
            timerHandler?.scrollNextPage()
            mHandler.postDelayed(this , interval)
        }
    }

//    override fun handleMessage(msg: Message) {
//        super.handleMessage(msg)
//        if (TIME_SCROLL_INTERVAL_MSG == msg.what) {
//            timerHandler?.scrollNextPage()
//            tick()
//        }
//    }

    private fun tick() {
        mHandler.postDelayed(mRunnable , interval)
//        sendEmptyMessageDelayed(TIME_SCROLL_INTERVAL_MSG, interval)
    }

    /**
     * 开始轮播
     */
    fun startTimer() {
        if (!isStopped) return
        mHandler.removeCallbacks(mRunnable)
        tick()
        isStopped = false
    }

    /**
     * 停止轮播
     */
    fun stopTimer() {
        if (isStopped) {
            return
        }
        mHandler.removeCallbacks(mRunnable)
        isStopped = true
    }

    fun interface TimerHandlerListener {
        fun scrollNextPage()
    }

    companion object {
        private const val TIME_SCROLL_INTERVAL_MSG = 0X87108
    }

}