package com.joyrun.banner

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.*
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import java.lang.reflect.InvocationTargetException


/**
 * author: wenjie
 * date: 2019-12-19 14:21
 * descption:
 */
class JoyrunBanner : RoundedCornersLayout, BannerTimerHandler.TimerHandlerListener , DefaultLifecycleObserver {

    private val mContext: Context

    private val compositePageTransformer: CompositePageTransformer

    private var bannerTimerHandler: BannerTimerHandler? = null

    private var mIndicatorPosition = Gravity.CENTER

    private var bannerAdapterWrapper: BannerAdapterWrapper<*>? = null


    /**
     * 是否可以手动滑动翻页
     */
    private var userInputEnabled = true

    /**
     * 是否循环轮播
     */
    private var enableLoop = true

    /**
     * 是否自动轮播 默认true
     */
    private var autoPlay = true

    /**
     * 自动轮播间隔时间默认3000ms
     */
    private var autoPlayTime = 3000

    private var mPageChangeDuration = 0L

    /**
     * 宽高比
     */
    private var mAspectRatio: String? = null

    private var indicatorTopMargin = 16f
    private var indicatorBottomMargin = 16f
    private var indicatorLeftMargin = 16f
    private var indicatorRightMargin = 16f

    private var mRatio = 0f

    private var mOnPageChangeListener: ViewPager2.OnPageChangeCallback? = null

    private val viewPager2: ViewPager2

    private var indicatorView: IndicatorView? = null

    private var attachedToWindow = true

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(
        context, attrs, defStyleAttr
    ) {
        mContext = context
        LayoutInflater.from(mContext).inflate(R.layout.joyrun_banner_layout, this)
        viewPager2 = findViewById(R.id.viewpager)
        initAttrs(context, attrs)

        compositePageTransformer = CompositePageTransformer()
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager2.isUserInputEnabled = userInputEnabled
        viewPager2.setPageTransformer(compositePageTransformer)
        viewPager2.registerOnPageChangeCallback(mOnPageChangeCallback)
        initViewPagerScrollProxy()
    }


    /**
     * 初始化自定义属性
     */
    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.JoyrunBanner)
        mIndicatorPosition = typedArray.getInt(
            R.styleable.JoyrunBanner_indicatorPosition, Gravity.CENTER or Gravity.BOTTOM
        )
        enableLoop = typedArray.getBoolean(R.styleable.JoyrunBanner_enableLoop, true)
        userInputEnabled = typedArray.getBoolean(R.styleable.JoyrunBanner_userInputEnabled, true)
        autoPlay = typedArray.getBoolean(R.styleable.JoyrunBanner_autoPlay, true)
        autoPlayTime = typedArray.getInteger(R.styleable.JoyrunBanner_autoPlayTime, autoPlayTime)
        mPageChangeDuration =
            typedArray.getInteger(R.styleable.JoyrunBanner_pageChangeDuration, 800).toLong()
        indicatorBottomMargin =
            typedArray.getDimension(R.styleable.JoyrunBanner_indicatorBottomMargin, 16f)
        indicatorTopMargin =
            typedArray.getDimension(R.styleable.JoyrunBanner_indicatorTopMargin, 16f)
        indicatorLeftMargin =
            typedArray.getDimension(R.styleable.JoyrunBanner_indicatorLeftMargin, 16f)
        indicatorRightMargin =
            typedArray.getDimension(R.styleable.JoyrunBanner_indicatorRightMargin, 16f)
        setRadius(typedArray.getDimension(R.styleable.JoyrunBanner_border_radius, 0f))
        mAspectRatio = typedArray.getString(R.styleable.JoyrunBanner_aspect_ratio)
        val ratios = mAspectRatio?.split(":")
        if (ratios?.size == 2) {
            mRatio = ratios[1].toFloat() / ratios[0].toFloat()
        }
        typedArray.recycle()

        initIndicator().setOrientation(ViewPager2.ORIENTATION_HORIZONTAL).setMargin(
            indicatorLeftMargin.toInt(),
            indicatorTopMargin.toInt(),
            indicatorRightMargin.toInt(),
            indicatorBottomMargin.toInt()
        ).setGravity(mIndicatorPosition)
            .setNormalColor(Color.parseColor("#d5d9d9"))
            .setFocusColor(Color.WHITE)
            .setRadius(8)
            .setIndicatorPadding(16)
            .build()
    }

    /**
     * 初始化指示器
     */
    fun initIndicator(): Indicator {
        disableIndicator()
        return IndicatorView(mContext).apply {
            indicatorView = this
            setBanner(this@JoyrunBanner)
            setIndicatorBuildListener {
                removeView(indicatorView)
                addView(
                    indicatorView, ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            }
        }
    }

    /**
     * 不显示指示器
     */
    fun disableIndicator(): JoyrunBanner {
        return also {
            indicatorView?.let {
                removeView(it)
            }
            indicatorView = null
        }
    }

    /**
     * 是否自动轮播
     */
    fun setAutoPlay(autoPlay: Boolean): JoyrunBanner {
        this.autoPlay = autoPlay
        return also {
            if (autoPlay && getRealCount() > 1) {
                bannerTimerHandler ?: BannerTimerHandler(
                    timerHandler = this, interval = autoPlayTime.toLong()
                ).also {
                    bannerTimerHandler = it
                    it.startTimer()
                }
            }
        }
    }

    fun setAutoPlatTime(autoPlayTime: Int) {
        this.autoPlayTime = autoPlayTime
    }

    fun setPagerScrollDuration(pagerScrollDuration: Long): JoyrunBanner {
        this.mPageChangeDuration = pagerScrollDuration
        return this
    }

    fun addPageTransformer(transformer: ViewPager2.PageTransformer): JoyrunBanner {
        return also {
            compositePageTransformer.addTransformer(transformer)
        }
    }

    fun setOrientation(@ViewPager2.Orientation orientation: Int): JoyrunBanner {
        viewPager2.orientation = orientation
        return this
    }

    /**
     * 是否支持手势滑动
     */
    fun setUserInputEnabled(enabled: Boolean): JoyrunBanner {
        return also {
            viewPager2.isUserInputEnabled = enabled
        }
    }

    /**
     * 开始自动轮播
     */
    fun startPlay(): JoyrunBanner {
        return also {
            bannerTimerHandler?.startTimer()
        }
    }

    /**
     * 停止自动轮播
     */
    fun stopPlay(): JoyrunBanner {
        return also {
            bannerTimerHandler?.stopTimer()
        }
    }

    /**
     * 是否循环轮播 默认'是'
     */
    fun setEnableLoop(enableLoop: Boolean): JoyrunBanner {
        return also {
            this.enableLoop = enableLoop
        }
    }

    fun addItemDecoration(decor: ItemDecoration): JoyrunBanner {
        viewPager2.addItemDecoration(decor)
        return this
    }

    fun isAutoPlay(): Boolean {
        return autoPlay && getRealCount() > 1
    }

    /**
     * 设置当前页
     */
    fun setCurrentItem(position: Int, smoothScroll: Boolean) {
        viewPager2.setCurrentItem(position, smoothScroll)
    }

    fun setOffscreenPageLimit(limit: Int) : JoyrunBanner{
        viewPager2.offscreenPageLimit = limit
        return this
    }

    /**
     * 获取当前页
     */
    fun getCurrentItem(): Int {
        val position: Int = viewPager2.currentItem
        return getRealPosition(position, getRealCount())
    }

    fun setAdapter(adapter: Adapter<*>?): JoyrunBanner {
        return also {
            adapter?.let {
                bannerAdapterWrapper = BannerAdapterWrapper(adapter.also {
                    it.registerAdapterDataObserver(itemDataSetChangeObserver)
                })
                viewPager2.adapter = bannerAdapterWrapper
            }
        }
    }

    val adapter: Adapter<*>?
        get() = bannerAdapterWrapper?.adapter

    /**
     * 设置滑动监听事件
     * java
     */
    fun setOnBannerChangeListener(onBannerChangeListener: ViewPager2.OnPageChangeCallback) : JoyrunBanner {
        this.mOnPageChangeListener = onBannerChangeListener
        return this
    }


    /**
     * 设置滑动监听事件
     * kotlin
     */
    inline fun setOnBannerSelectedListener(crossinline onPageSelected: (position: Int) -> Unit) {
        setOnBannerChangeListener(onPageSelected = onPageSelected)
    }

    /**
     * 设置滑动监听事件
     * kotlin
     */
    @JvmOverloads
    inline fun setOnBannerChangeListener(
        crossinline onPageScrolled: ((
            position: Int, positionOffset: Float, positionOffsetPixels: Int
        ) -> Unit) = { _, _, _ -> },
        crossinline onPageScrollStateChanged: ((state: Int) -> Unit) = { _ -> },
        crossinline onPageSelected: ((position: Int) -> Unit) = { _ -> }
    ) {
        val listener = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                onPageScrollStateChanged.invoke(state)
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                onPageScrolled.invoke(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                onPageSelected.invoke(position)
            }
        }
        setOnBannerChangeListener(listener)
    }

    /**
     * 按照宽高比例进行测量显示
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        if (mRatio > 0) {
            val heightMeasure =
                MeasureSpec.makeMeasureSpec((widthSize * mRatio).toInt(), MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, heightMeasure)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        if (action == MotionEvent.ACTION_DOWN) {
            stopPlay()
        }
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            startPlay()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun scrollNextPage() {
        if (getRealCount() > 0) {
            viewPager2.setCurrentItem(viewPager2.currentItem + 1, true)
        }
    }

    private fun getRealCount(): Int {
        return bannerAdapterWrapper?.getRealCount() ?: 0
    }

    fun getRealPosition(position: Int, pageSize: Int): Int {
        return if (pageSize == 0) {
            0
        } else if (enableLoop) {
            (position + pageSize) % pageSize
        } else {
            position
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        attachedToWindow = false
        if (isAutoPlay()) {
            bannerTimerHandler?.stopTimer()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        attachedToWindow = true
        if (isAutoPlay()) {
            bannerTimerHandler?.startTimer()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun changed() {
        bannerAdapterWrapper?.let {
//            viewPager2.adapter = it
            if (enableLoop) {
                val item = it.itemCount / 2
                viewPager2.setCurrentItem(item, false)
            }
            it.notifyDataSetChanged()
        }
        setAutoPlay(isAutoPlay())
        indicatorView?.initIndicator(getRealCount())
    }


    private fun initViewPagerScrollProxy() {
        try {
            //控制切换速度，采用反射方。法方法只会调用一次，替换掉内部的RecyclerView的LinearLayoutManager
            val recyclerView = viewPager2.getChildAt(0) as RecyclerView
            recyclerView.overScrollMode = OVER_SCROLL_NEVER
            val linearLayoutManager = recyclerView.layoutManager as? LinearLayoutManager
            val proxyLayoutManger = ProxyLayoutManger(mContext, linearLayoutManager)
            recyclerView.layoutManager = proxyLayoutManger

            val mRecyclerView = LayoutManager::class.java.getDeclaredField("mRecyclerView")
            mRecyclerView.isAccessible = true
            mRecyclerView.set(linearLayoutManager, recyclerView)

            val layoutMangerField = ViewPager2::class.java.getDeclaredField("mLayoutManager")
            layoutMangerField.isAccessible = true
            layoutMangerField.set(viewPager2, proxyLayoutManger)

            val pageTransformerAdapterField =
                ViewPager2::class.java.getDeclaredField("mPageTransformerAdapter")
            pageTransformerAdapterField.isAccessible = true
            val mPageTransformerAdapter = pageTransformerAdapterField.get(viewPager2)
            mPageTransformerAdapter?.let {
                val clazz = mPageTransformerAdapter.javaClass
                val layoutManager = clazz.getDeclaredField("mLayoutManager")
                layoutManager.isAccessible = true
                layoutManager.set(mPageTransformerAdapter, proxyLayoutManger)
            }
            val scrollEventAdapterField =
                ViewPager2::class.java.getDeclaredField("mScrollEventAdapter")
            scrollEventAdapterField.isAccessible = true
            val mScrollEventAdapter = scrollEventAdapterField.get(viewPager2)
            mScrollEventAdapter?.let {
                val clazz = mScrollEventAdapter.javaClass
                val layoutManager = clazz.getDeclaredField("mLayoutManager")
                layoutManager.isAccessible = true
                layoutManager.set(mScrollEventAdapter, proxyLayoutManger)
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    private inner class ProxyLayoutManger(
        context: Context,
        private val layoutManager: LinearLayoutManager?
    ) : LinearLayoutManager(context, layoutManager?.orientation ?: HORIZONTAL, false) {

        override fun performAccessibilityAction(
            recycler: Recycler,
            state: State, action: Int, args: Bundle?
        ): Boolean {
            return layoutManager?.performAccessibilityAction(recycler, state, action, args)
                ?: super.performAccessibilityAction(recycler, state, action, args)
        }

        override fun onInitializeAccessibilityNodeInfo(
            recycler: Recycler,
            state: State, info: AccessibilityNodeInfoCompat
        ) {
            layoutManager?.onInitializeAccessibilityNodeInfo(recycler, state, info)
        }

        override fun requestChildRectangleOnScreen(
            parent: RecyclerView,
            child: View, rect: Rect, immediate: Boolean,
            focusedChildVisible: Boolean
        ): Boolean {
            return layoutManager?.requestChildRectangleOnScreen(
                parent,
                child,
                rect,
                immediate,
                focusedChildVisible
            ) ?: super.requestChildRectangleOnScreen(
                parent,
                child,
                rect,
                immediate,
                focusedChildVisible
            )
        }

        override fun calculateExtraLayoutSpace(
            state: State,
            extraLayoutSpace: IntArray
        ) {
            try {
                val method = layoutManager?.javaClass?.getDeclaredMethod(
                    "calculateExtraLayoutSpace",
                    state.javaClass,
                    extraLayoutSpace.javaClass
                )
                method?.isAccessible = true
                method?.invoke(layoutManager, state, extraLayoutSpace)
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }


        override fun smoothScrollToPosition(
            recyclerView: RecyclerView,
            state: State,
            position: Int
        ) {
            recyclerView.context?.let {
                val linearSmoothScroller = object : LinearSmoothScroller(it) {
                    override fun calculateTimeForDeceleration(dx: Int): Int {
                        return (mPageChangeDuration * (1 - .3356)).toInt()
                    }
                }
                linearSmoothScroller.targetPosition = position
                startSmoothScroll(linearSmoothScroller)
            }

        }
    }

    private val mOnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            mOnPageChangeListener?.onPageScrollStateChanged(state)
            indicatorView?.onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(
            position: Int, positionOffset: Float, positionOffsetPixels: Int
        ) {
            val realPosition = getRealPosition(position, getRealCount())
            mOnPageChangeListener?.onPageScrolled(
                realPosition, positionOffset, positionOffsetPixels
            )
            indicatorView?.onPageScrolled(realPosition, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            val realPosition: Int = getRealPosition(position, getRealCount())
            mOnPageChangeListener?.onPageSelected(realPosition)
            indicatorView?.onPageSelected(realPosition)
        }
    }


    private inner class BannerAdapterWrapper<H : ViewHolder>(val adapter: Adapter<H>) :
        Adapter<H>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
            return adapter.onCreateViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: H, position: Int) {
            adapter.onBindViewHolder(holder, getRealPosition(position, getRealCount()))
        }

        override fun getItemCount(): Int {
            return if (getRealCount() > 1 && enableLoop) {
                getRealCount() * 400
            } else {
                getRealCount()
            }
        }

        fun getRealCount(): Int {
            return adapter.itemCount
        }

        override fun getItemViewType(position: Int): Int {
            return adapter.getItemViewType(getRealPosition(position, getRealCount()))
        }
    }


    private val itemDataSetChangeObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            changed()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            onChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            onChanged()
        }
    }


    /**------------------------------------------Lifecycle------------------------------------------**/
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        if (attachedToWindow){
            bannerTimerHandler?.startTimer()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        bannerTimerHandler?.stopTimer()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        bannerTimerHandler?.stopTimer()
        viewPager2.unregisterOnPageChangeCallback(mOnPageChangeCallback)
    }
}

