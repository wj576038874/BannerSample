package com.joyrun.bannersample

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.joyrun.banner.JoyRunBanner
import kotlinx.android.synthetic.main.content_scrolling.*


class ScrollingActivity : AppCompatActivity() {

    private lateinit var joyRunBanner: JoyRunBanner<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_scrolling)

        joyRunBanner = findViewById(R.id.banner)


        scrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            val bol = isVisibleLocal(joyRunBanner)
            Log.e("asd" , "$bol")
        }


        btn_next.setOnClickListener {
            joyRunBanner.getViewPager().scrollNextPage()
        }

        joyRunBanner.setBannerLoadAdapter { joyRunBanner, data, itemView, position ->
            val imageView: ImageView = itemView.findViewById(R.id.image)
            val mf = MultiTransformation(CenterCrop(), RoundedCorners(40))



            Glide.with(this).load(data).apply(RequestOptions.bitmapTransform(mf)).into(imageView)
        }
        val d = listOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665548&di=efe0920c74ffae46d80bf9302e0ff67c&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb151f8198618367aa7f3cc7424738bd4b31ce525.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=7f4e5dac527d55168d7f29d9aeaad01b&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F9a504fc2d5628535bdaac29e9aef76c6a6ef63c2.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=680286202b1d5e1423298e0cfb392568&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F810a19d8bc3eb1354c94a704ac1ea8d3fd1f4439.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665546&di=6d699d47ec867d1d135c5bdb4daacbcc&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F21a4462309f7905296a7578106f3d7ca7acbd5d0.jpg")

        joyRunBanner.initIndicator()
            .setFocusResId(R.drawable.ic_dot_press)
            .setNormalResId(R.drawable.ic_dot_normal)
            .setIndicatorPadding(10)
            .build()
        joyRunBanner.setScrollDuration(500)
        joyRunBanner.setBannerData(R.layout.my_banner_item ,
            d
        )



//        joyRunBanner.setLoop(false)

        joyRunBanner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
//                Log.e("asd", "onPageSelected" + position)
            }

        })
    }

    fun isVisibleLocal(target: View): Boolean {
        val rect = Rect()
        target.getLocalVisibleRect(rect)
        return rect.top >= 0
    }
}
