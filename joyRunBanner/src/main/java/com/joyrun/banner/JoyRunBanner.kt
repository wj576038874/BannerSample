package com.joyrun.banner

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.joyrun.banner.viewpager.IUltraIndicatorBuilder
import com.joyrun.banner.viewpager.PageStyle
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

    private val mContext: Context = context

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


    private var mPageChangeDuration = 0

    private var mAspectRatio: String? = null

    private var mRatio = 0f

    /**
     * 默认动画插值器 在动画开始的地方快然后慢
     */
    private var mInterpolator: Interpolator = DecelerateInterpolator()

    private var mData = listOf<T>()

    private var mTransformer = Transformer.Default

    private var mPageStyle = PageStyle.DEFAULT

    private var mItemViewSpacing = 16f

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
        LayoutInflater.from(mContext).inflate(R.layout.joyrun_banner_layout, this)

        initAttrs(context, attrs)

        setScrollDuration(mPageChangeDuration)
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
        enableLoop = typedArray.getBoolean(R.styleable.JoyRunBanner_enableLoop, true)
        userInputEnabled = typedArray.getBoolean(R.styleable.JoyRunBanner_userInputEnabled, true)
        autoPlay = typedArray.getBoolean(R.styleable.JoyRunBanner_autoPlay, true)
        autoPlayTime = typedArray.getInteger(R.styleable.JoyRunBanner_autoPlayTime, autoPlayTime)
        mPageChangeDuration = typedArray.getInteger(R.styleable.JoyRunBanner_pageChangeDuration, 0)
        setRadius(typedArray.getDimension(R.styleable.JoyRunBanner_border_radius, 0f))
        mAspectRatio = typedArray.getString(R.styleable.JoyRunBanner_aspect_ratio)
        val ratios = mAspectRatio?.split(":")
        if (ratios?.size == 2) {
            mRatio = ratios[1].toFloat() / ratios[0].toFloat()
        }
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
                .setMargin(16, 16, 16, 16)
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
            //大于1循环
            setEnableLoop(enableLoop && data.size > 1)
            adapter = BannerPageAdapter()
            when (mPageStyle) {
                PageStyle.MULTI_PAGE -> {
                    bannerViewPager.setMultiScreen(0.9f)
                    mItemViewSpacing /= 2
                }
                PageStyle.MULTI_PAGE_SCALE -> {
                    bannerViewPager.setMultiScreen(0.8f)
                    mTransformer = Transformer.Scale
                }
                PageStyle.MARGIN_PAGE -> {
                    bannerViewPager.setItemMargin(
                        dp2px(mContext, mItemViewSpacing),
                        0,
                        dp2px(mContext, mItemViewSpacing),
                        0
                    )
                }
                PageStyle.DEFAULT -> {

                }
            }
            setPageTransformer(false, BasePageTransformer.getPageTransformer(mTransformer))
            bannerViewPager.setOnPageChangeListener(this@JoyRunBanner)
        }
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
     * page style
     * @see PageStyle
     * 如果需要item为圆角的haunt
     * 去除joyrunbanner的圆角 再{#bannerLoadAdapter}自行设置imageview的圆角即可
     */
    fun setPageStyle(pageStyle: PageStyle) {
        mTransformer = Transformer.Default
        this.mPageStyle = pageStyle
    }

    /**
     * @param itemViewSpacing 间距 单位 dp
     * 针对pageStyle为MULTI_PAGE 和 MARGIN_PAGE 的间距
     */
    fun setPageStyle(pageStyle: PageStyle, itemViewSpacing: Int) {
        setPageStyle(pageStyle)
        this.mItemViewSpacing = itemViewSpacing.toFloat()
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

    /**
     * 设置viewpager页面切换速度
     */
    fun setScrollDuration(duration: Int) {
        this.mPageChangeDuration = duration
        if (mPageChangeDuration == 0) return
        bannerViewPager.setScrollDuration(mInterpolator, mPageChangeDuration)
    }

    /**
     * 开始自动轮播
     */
    fun startPaly() {
        bannerViewPager.startTimer()
    }

    /**
     * 停止自动轮播
     */
    fun stopPlay() {
        bannerViewPager.stopTimer()
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
        this.mPageChangeDuration = duration
        if (mPageChangeDuration == 0) return
        this.mInterpolator = interpolator
        bannerViewPager.setScrollDuration(mInterpolator, mPageChangeDuration)
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
    fun setEnableLoop(enableLoop: Boolean) {
        bannerViewPager.setInfiniteLoop(enableLoop)
    }

    /**
     * 获取数据
     */
    val data: List<T>
        get() = mData

    /**
     * 获取viewpager
     */
    val viewPager: UltraViewPager
        get() = bannerViewPager

    /**
     * 设置图片加载器 回到给使用者自行加载，框架不知道广告图片的url具体是哪个字段
     */
    fun setBannerLoadAdapter(bannerLoadAdapter: (joyRunBanner: JoyRunBanner<T>, item: T, itemView: View, position: Int) -> Unit) {
        mBannerLoadAdapter = bannerLoadAdapter
    }

    /**
     * 设置banner的点击事件
     */
    fun setOnBannerItemClickListener(onBannerItemClickListener: (joyRunBanner: JoyRunBanner<T>, item: T, itemView: View, position: Int) -> Unit) {
        mOnBannerItemClickListener = onBannerItemClickListener
    }


    fun setOnPageChangeListener(onPageChangeListener: ViewPager.OnPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener
    }


    private inner class BannerPageAdapter : PagerAdapter() {

        override fun isViewFromObject(view: View, item: Any): Boolean = view == item

        override fun getCount(): Int = mData.size

        override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
            container.removeView(item as View)
            Log.e("asd", "destroyItem$position")
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            Log.e("asd", "instantiateItem$position")
            val itemView = LayoutInflater.from(container.context)
                .inflate(mBannerLayoutRes, container, false)
            itemView.layerType
            if (mPageStyle == PageStyle.MULTI_PAGE) {
                itemView.setPadding(
                    dp2px(mContext, mItemViewSpacing),
                    0,
                    dp2px(mContext, mItemViewSpacing),
                    0
                )
            }
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

    fun setCurrentItem(position: Int, smoothScroll: Boolean = true) {
        viewPager.setCurrentItem(position, smoothScroll)
    }

    fun getCurrentItem() = viewPager.currentItem


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
}