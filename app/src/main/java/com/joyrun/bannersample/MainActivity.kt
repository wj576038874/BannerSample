package com.joyrun.bannersample

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.joyrun.banner.JoyRunBanner
import com.joyrun.banner.viewpager.BitmapUtil
import com.joyrun.banner.viewpager.transformer.Transformer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var joyRunBanner: JoyRunBanner<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbFlow =DBFlow()
        val greenDao = GreenDao()
        val litepal = Litepal()

        val myDB = MyDB(dbFlow)
        val myDB2 = MyDB(greenDao)
        val myDB3 = MyDB(litepal)

        joyRunBanner = findViewById(R.id.JoyRunBanner)

        joyRunBanner.setBannerLoadAdapter { joyRunBanner, data, itemView, position ->
            val imageView:ImageView = itemView.findViewById(R.id.image)
            val mf = MultiTransformation(CenterCrop(),RoundedCorners(20))



            Glide.with(this@MainActivity).load(data).into(imageView)
        }



        joyRunBanner.setOnBannerItemClickListener { joyRunBanner, bannerItem, view, position ->
            Toast.makeText(this@MainActivity , position.toString()+bannerItem , Toast.LENGTH_SHORT).show()
        }




//        btn_stop.setOnClickListener {
//            JoyRunBanner.stopAutoPlay()
//        }
//
        btn_start.setOnClickListener {
//            val bitmap = BitmapFactory.decodeResource(resources ,R.drawable.ic_launcher_background)
//            val b = BitmapUtil.drawableToBitmap(getDrawable(R.drawable.joyrun_banner_shape_point_selected))
//            val bitmap2 : GradientDrawable = ContextCompat.getDrawable(this , R.drawable.joyrun_banner_shape_point_normal) as GradientDrawable
//            Log.e("asd" , bitmap.toString())
            startActivity(Intent(this , Main2Activity::class.java))
        }

        val d = listOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665548&di=efe0920c74ffae46d80bf9302e0ff67c&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb151f8198618367aa7f3cc7424738bd4b31ce525.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=7f4e5dac527d55168d7f29d9aeaad01b&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F9a504fc2d5628535bdaac29e9aef76c6a6ef63c2.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=680286202b1d5e1423298e0cfb392568&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F810a19d8bc3eb1354c94a704ac1ea8d3fd1f4439.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665546&di=6d699d47ec867d1d135c5bdb4daacbcc&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F21a4462309f7905296a7578106f3d7ca7acbd5d0.jpg")

        btn_refresh.setOnClickListener {
            joyRunBanner.refreshBannerData(listOf(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=7f4e5dac527d55168d7f29d9aeaad01b&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F9a504fc2d5628535bdaac29e9aef76c6a6ef63c2.jpg",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=680286202b1d5e1423298e0cfb392568&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F810a19d8bc3eb1354c94a704ac1ea8d3fd1f4439.jpg"
            ))

        }

//        JoyRunBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                Log.e("asd", "onPageSelected" + position)
//            }
//        })



        joyRunBanner.initIndicator()
            .setFocusResId(R.drawable.ic_dot_press)
            .setNormalResId(R.drawable.ic_dot_normal)
            .setIndicatorPadding(10)
            .build()

        joyRunBanner.setBannerData(R.layout.my_banner_item ,
            d
        )

        joyRunBanner.setScrollDuration(LinearInterpolator(),500)

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
                Log.e("asd", "onPageSelected" + position)
            }

        })

    }
}
