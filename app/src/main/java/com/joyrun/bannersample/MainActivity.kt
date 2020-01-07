package com.joyrun.bannersample

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.*
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
import com.joyrun.banner.viewpager.PageStyle
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
            val mf = MultiTransformation(CenterCrop(),RoundedCorners(40))



            Glide.with(this@MainActivity).load(data).apply(RequestOptions.bitmapTransform(mf)).into(imageView)
        }



        joyRunBanner.setOnBannerItemClickListener { joyRunBanner, bannerItem, view, position ->
            Toast.makeText(this@MainActivity , position.toString()+bannerItem , Toast.LENGTH_SHORT).show()
        }


        btn_stop.setOnClickListener {
            JoyRunBanner.stopPlay()
        }
//
        btn_start.setOnClickListener {
//            val bitmap = BitmapFactory.decodeResource(resources ,R.drawable.ic_launcher_background)
//            val b = BitmapUtil.drawableToBitmap(getDrawable(R.drawable.joyrun_banner_shape_point_selected))
//            val bitmap2 : GradientDrawable = ContextCompat.getDrawable(this , R.drawable.joyrun_banner_shape_point_normal) as GradientDrawable
//            Log.e("asd" , bitmap.toString())
            startActivity(Intent(this , Main2Activity::class.java))
//            joyRunBanner.getViewPager().startTimer()
        }

        btn_stop.setOnClickListener {
//            joyRunBanner.setScrollDuration(DecelerateInterpolator() , 600)
            joyRunBanner.getViewPager().stopTimer()
        }

        val data = listOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665548&di=efe0920c74ffae46d80bf9302e0ff67c&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb151f8198618367aa7f3cc7424738bd4b31ce525.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=7f4e5dac527d55168d7f29d9aeaad01b&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F9a504fc2d5628535bdaac29e9aef76c6a6ef63c2.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=680286202b1d5e1423298e0cfb392568&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F810a19d8bc3eb1354c94a704ac1ea8d3fd1f4439.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665546&di=6d699d47ec867d1d135c5bdb4daacbcc&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F21a4462309f7905296a7578106f3d7ca7acbd5d0.jpg")

        btn_refresh.setOnClickListener {
            joyRunBanner.refreshBannerData(listOf(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578397987333&di=49a0e58e716879f19562c57bbe26981a&imgtype=0&src=http%3A%2F%2Ft8.baidu.com%2Fit%2Fu%3D2247852322%2C986532796%26fm%3D79%26app%3D86%26f%3DJPEG%3Fw%3D1280%26h%3D853",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578397987331&di=706d56b72ad15ae5b0a6c49787e55335&imgtype=0&src=http%3A%2F%2Ft9.baidu.com%2Fit%2Fu%3D583874135%2C70653437%26fm%3D79%26app%3D86%26f%3DJPEG%3Fw%3D3607%26h%3D2408",
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578398034097&di=4d7bdc1390087488326fae46167f2378&imgtype=0&src=http%3A%2F%2Ft8.baidu.com%2Fit%2Fu%3D2983382668%2C1612769941%26fm%3D79%26app%3D86%26f%3DJPEG%3Fw%3D1280%26h%3D854"
            ))
        }



        joyRunBanner.initIndicator()
            .setFocusResId(R.drawable.ic_dot_press)
            .setNormalResId(R.drawable.ic_dot_normal)
            .setIndicatorPadding(10)
            .build()
        joyRunBanner.setScrollDuration(500)
        joyRunBanner.setPageStyle(PageStyle.MULTI_PAGE , 16f)
        joyRunBanner.setBannerData(R.layout.my_banner_item , data)

//        joyRunBanner.setLoop(false)


        mulit_page.setOnClickListener {
            joyRunBanner.setScrollDuration(500)
            joyRunBanner.setPageStyle(PageStyle.MULTI_PAGE,16f)
            joyRunBanner.setBannerData(R.layout.my_banner_item , data)
        }

        mulit_page_scale.setOnClickListener {
            joyRunBanner.setScrollDuration(500)
            joyRunBanner.setPageStyle(PageStyle.MULTI_PAGE_SCALE)
            joyRunBanner.setBannerData(R.layout.my_banner_item , data)
        }

        mulit_maggin_page.setOnClickListener {
            joyRunBanner.setScrollDuration(500)
            joyRunBanner.setPageStyle(PageStyle.MAGIN_PAGE , 16f)
            joyRunBanner.setBannerData(R.layout.my_banner_item , data)
        }

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


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("asd" , "onNewIntent")
    }

    override fun onStart() {
        super.onStart()
        Log.e("asd" , "MainActivity onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("asd" , "MainActivity onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("asd" , "MainActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("asd" , "MainActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("asd" , "MainActivity onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("asd" , "MainActivity onDestroy")
    }


}
