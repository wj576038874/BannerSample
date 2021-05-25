package com.joyrun.bannersample

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.bumptech.glide.Glide
import com.joyrun.banner.JoyRunBanner

/**
 * author: wenjie
 * date: 2019-11-12 15:51
 * descption: 社区推荐动态顶部轮播banner
 */
class BannerItemView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : ConstraintLayout(context, attr, defStyleAttr) {

    private val mContext: Context = context
    private val recommendBanner: JoyRunBanner<String>

    init {
        LayoutInflater.from(context).inflate(R.layout.item_community_banner_topic_list, this)

        recommendBanner = findViewById(R.id.recommend_banner)
        recommendBanner.setScrollDuration(500)
        //定义指示器
        recommendBanner.initIndicator()
            .setFocusResId(R.drawable.ic_dot_press)
            .setNormalResId(R.drawable.ic_dot_normal)
            .setIndicatorPadding(10)
            .build()
        recommendBanner.setBannerLoadAdapter { _, data, itemView, _ ->
            val imageCover: ImageView = itemView.findViewById(R.id.iv_banner_cover)
            Glide.with(mContext)
                .load(data)
                .into(imageCover)
        }

        recommendBanner.setOnBannerItemClickListener { _, bannerData, _, position ->

        }

    }

    fun setBanners(bannerTopics: List<String>) {
        recommendBanner.setBannerData(R.layout.item_community_banner_topic, bannerTopics)
    }

}