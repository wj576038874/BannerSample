package com.joyrun.banner

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.joyrun.banner.viewpager.IUltraIndicatorBuilder
import com.joyrun.banner.viewpager.UltraViewPager
import com.joyrun.banner.viewpager.transformer.BasePageTransformer
import com.joyrun.banner.viewpager.transformer.Transformer
import kotlinx.android.synthetic.main.joyrun_banner_layout.view.*


/**
 * author: wenjie
 * date: 2019-12-19 14:21
 * descption:
 */
class JoyRunBanner<T> : RoundedCornersLayout, ViewPager.OnPageChangeListener {

    private val mContext: Context

    @DrawableRes
    private var mPointNormal = R.drawable.joyrun_banner_shape_point_normal

    @DrawableRes
    private var mPointSelected = R.drawable.joyrun_banner_shape_point_selected

    @LayoutRes
    private var mBannerLayoutRes = R.layout.joyrun_banner_item_image

    private var mPointLocation = Gravity.CENTER

    /**
     * 是否展示point
     */
    private var showPointContainer = true

    /**
     * 是否可以手动滑动翻页
     */
    private var userInputEnabled = true

    /**
     * 是否自动轮播 默认true
     */
    private var autoPlay = true

    /**
     * 自动轮播间隔时间默认5000ms
     */
    private var autoPlayTime = 5000

    private var mData = listOf<T>()

    private var mTransformer = Transformer.Default


    private var mBannerLoadAdapter: ((joyRunBanner: JoyRunBanner<T>, data: T, itemView: View, position: Int) -> Unit)? =
        null

    private var mOnBannerItemClickListener: ((joyRunBanner: JoyRunBanner<T>, data: T, itemView: View, position: Int) -> Unit)? =
        null

    private var mOnPageChangeListener: ViewPager.OnPageChangeListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mContext = context
        LayoutInflater.from(mContext).inflate(R.layout.joyrun_banner_layout, this)

        initAttrs(context, attrs)
    }

    /**
     * 初始化自定义属性
     */
    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.JoyRunBanner)
        mPointNormal = typedArray.getResourceId(
            R.styleable.JoyRunBanner_pointNormal,
            R.drawable.joyrun_banner_shape_point_normal
        )
        mPointSelected = typedArray.getResourceId(
            R.styleable.JoyRunBanner_pointSelected,
            R.drawable.joyrun_banner_shape_point_selected
        )
        mPointLocation = typedArray.getInt(R.styleable.JoyRunBanner_pointsLocation, 1)
        showPointContainer =
            typedArray.getBoolean(R.styleable.JoyRunBanner_showPointContainer, true)
        userInputEnabled = typedArray.getBoolean(R.styleable.JoyRunBanner_userInputEnabled, true)
        autoPlay = typedArray.getBoolean(R.styleable.JoyRunBanner_autoPlay, true)
        autoPlayTime = typedArray.getInteger(R.styleable.JoyRunBanner_autoPlayTime, 5000)
        setRadius(typedArray.getDimension(R.styleable.JoyRunBanner_border_radius, 0f))
        typedArray.recycle()
    }


    /**
     * 代码设置指示点 会覆盖xml文件中定义的属性
     */
    fun initIndicator(): IUltraIndicatorBuilder {
        showPointContainer = false
        bannerViewPager.initIndicator()
        return bannerViewPager.indicator
    }


    /**
     * 是否显示指示点
     */
    private fun showPoint(showPointContainer: Boolean) {
        if (showPointContainer) {
            bannerViewPager.initIndicator()
            bannerViewPager.indicator
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setMargin(0, 0, 0, dp2px(mContext, 8f))
                .setFocusDrawableId(mPointSelected)
                .setNormalDrawableId(mPointNormal)
                .setIndicatorPadding(10)
                .setGravity(
                    Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM or when (mPointLocation) {
                        0 -> Gravity.START
                        1 -> Gravity.CENTER
                        2 -> Gravity.END
                        else -> Gravity.CENTER
                    }
                )
                .build()
        }
    }

    /**
     * 是否自动轮播
     */
    private fun autoPlay(autoPlay: Boolean) {
        if (autoPlay) {
            bannerViewPager.setAutoScroll(autoPlayTime)
        }
    }

    /**
     * 设置banner数据
     * 如果数据只有一条不自动轮播且不循环，并且不显示指示点
     */
    fun setBannerData(data: List<T>) {
        this.mData = data
        bannerViewPager.apply {
            adapter = BannerPageAdapter()
            setPageTransformer(false, BasePageTransformer.getPageTransformer(mTransformer))
            bannerViewPager.setOnPageChangeListener(this@JoyRunBanner)
        }
        //大于1循环
        setLoop(data.size > 1)
        //大于1并且showPointContainer是true显示指示点
        showPoint(data.size > 1 && showPointContainer)
        //autoPlay为true并且大于1自动轮播
        autoPlay(autoPlay && data.size > 1)
    }

    /**
     * 携带动画 默认动画
     */
    fun setBannerData(data: List<T>, transformer: Transformer) {
        this.mTransformer = transformer
        setBannerData(data)
    }

    /**
     * 自定义加载布局
     */
    fun setBannerData(@LayoutRes layoutRes: Int, data: List<T>) {
        this.mBannerLayoutRes = layoutRes
        setBannerData(data)
    }

    fun setBannerData(@LayoutRes layoutRes: Int, data: List<T>, transformer: Transformer) {
        this.mBannerLayoutRes = layoutRes
        this.mTransformer = transformer
        setBannerData(data)
    }


    fun setScrollDuration(duration: Int) {
        bannerViewPager.setScrollDuration(duration)
    }


    /**
     * @param interpolator AccelerateDecelerateInterpolator 在动画开始与结束的地方速率改变比较慢，在中间的时候加速
     *                     AccelerateInterpolator  在动画开始的地方速率改变比较慢，然后开始加速
     *                     AnticipateInterpolator 开始的时候向后然后向前甩
     *                     AnticipateOvershootInterpolator 开始的时候向后然后向前甩一定值后返回最后的值
     *                     BounceInterpolator   动画结束的时候弹起
     *                     CycleInterpolator 动画循环播放特定的次数，速率改变沿着正弦曲线
     *                     DecelerateInterpolator 在动画开始的地方快然后慢
     *                     LinearInterpolator   以常量速率改变
     *                     OvershootInterpolator    向前甩一定值后再回到原来位置
     * @param duration     时长
     */
    fun setScrollDuration(interpolator: Interpolator, duration: Int) {
        bannerViewPager.setScrollDuration(interpolator, duration)
    }

    /**
     * 刷新数据
     */
    fun refreshBannerData(data: List<T>) {
        this.mData = data
        bannerViewPager.refresh()
    }

    /**
     * 是否循环轮播 默认'是'
     */
    fun setLoop(isLoop: Boolean) {
        bannerViewPager.setInfiniteLoop(isLoop)
    }


    fun getViewPager() = bannerViewPager

    /**
     * 设置图片加载器 回到给使用者自行加载，框架不知道广告图片的url具体是哪个字段
     */
    fun setBannerLoadAdapter(bannerLoadAdapter: (joyRunBanner: JoyRunBanner<T>, data: T, itemView: View, position: Int) -> Unit) {
        mBannerLoadAdapter = bannerLoadAdapter
    }

    /**
     * 设置banner的点击事件
     */
    fun setOnBannerItemClickListener(onBannerItemClickListener: (joyRunBanner: JoyRunBanner<T>, data: T, itemView: View, position: Int) -> Unit) {
        mOnBannerItemClickListener = onBannerItemClickListener
    }


    fun setOnPageChangeListener(onPageChangeListener: ViewPager.OnPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener
    }


    private inner class BannerPageAdapter : PagerAdapter() {

        override fun isViewFromObject(view: View, item: Any): Boolean = view === item

        override fun getCount(): Int = mData.size


        override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
            container.removeView(item as View)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView = LayoutInflater.from(container.context)
                .inflate(mBannerLayoutRes, container, false)

            itemView.setOnClickListener {
                mOnBannerItemClickListener?.invoke(
                    this@JoyRunBanner,
                    mData[position],
                    itemView,
                    position
                )
            }

            mBannerLoadAdapter?.invoke(this@JoyRunBanner, mData[position], itemView, position)

            container.addView(itemView)
            return itemView
        }

    }

    private fun dp2px(context: Context, dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.resources.displayMetrics
        ).toInt()
    }

    override fun onPageScrollStateChanged(state: Int) {
        mOnPageChangeListener?.onPageScrollStateChanged(state)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        mOnPageChangeListener?.onPageScrolled(
            bannerViewPager.currentItem,
            positionOffset,
            positionOffsetPixels
        )
    }

    override fun onPageSelected(position: Int) {
        mOnPageChangeListener?.onPageSelected(bannerViewPager.currentItem)
    }
}