package com.joyrun.bannersample

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.animation.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.joyrun.banner.JoyRunBanner
import com.joyrun.banner.viewpager.PageStyle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var joyRunBanner: JoyRunBanner<String>
    private var currentPosition2 = 1
    private var currentPosition = 1
    private val mHandler2: Handler = Handler()

//    private val mLoopRunnable2: Runnable = object : Runnable {
//        override fun run() {
//            currentPosition2 = viewpager.currentItem
//            currentPosition2++
//            //不需要为了循环轮播来判断是否到达最后一页，在监听器中已经为我们做了此操作
//            viewpager.currentItem = currentPosition2
//            mHandler2.postDelayed(this, 3000)
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbFlow = DBFlow()
        val greenDao = GreenDao()
        val litepal = Litepal()

        val myDB = MyDB(dbFlow)
        val myDB2 = MyDB(greenDao)
        val myDB3 = MyDB(litepal)

        val data = listOf(
            "https://joyrun-activity-upyun.thejoyrun.com/advert/fd93cff9f01243cb8d9a5c4641ba324e.png",
            "https://joyrun-activity-upyun.thejoyrun.com/advert/4e48fe4e8a5342b48d6bb55cfdb78bdd.jpg",
            "https://joyrun-activity-upyun.thejoyrun.com/advert/0efad2703ffa4d37a43bbdc6de1cca8f.jpg",
            "https://joyrun-activity-upyun.thejoyrun.com/advert/16e8b0ddcf054f91b183e4fe3e5493ff.jpg",
            "https://joyrun-activity-upyun.thejoyrun.com/advert/c373bc52acfa41ea97998471bfb4c504.jpg",
            "https://joyrun-activity-upyun.thejoyrun.com/advert/fd93cff9f01243cb8d9a5c4641ba324e.png",
            "https://joyrun-activity-upyun.thejoyrun.com/advert/4e48fe4e8a5342b48d6bb55cfdb78bdd.jpg"
        )

        joyRunBanner = findViewById(R.id.JoyRunBanner)


//        mHandler2.postDelayed(mLoopRunnable2, 3000)

        viewpager.offscreenPageLimit = 7
        viewpager.adapter = object : PagerAdapter() {

//            val data1 = listOf(
//                "https://joyrun-activity-upyun.thejoyrun.com/advert/4e48fe4e8a5342b48d6bb55cfdb78bdd.jpg",
//                "https://joyrun-activity-upyun.thejoyrun.com/advert/0efad2703ffa4d37a43bbdc6de1cca8f.jpg",
//                "https://joyrun-activity-upyun.thejoyrun.com/advert/16e8b0ddcf054f91b183e4fe3e5493ff.jpg",
//                "https://joyrun-activity-upyun.thejoyrun.com/advert/c373bc52acfa41ea97998471bfb4c504.jpg",
//                "https://joyrun-activity-upyun.thejoyrun.com/advert/fd93cff9f01243cb8d9a5c4641ba324e.png"
//            )

            override fun getCount(): Int {
                return data.size
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return `object` == view
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val itemView = LayoutInflater.from(container.context)
                    .inflate(R.layout.my_banner_item, container, false)
                val imageView: ImageView = itemView.findViewById(R.id.image)
                Glide.with(container.context).load(data[position])
                    .into(imageView)
                container.addView(itemView)
                return itemView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View)
            }
        }
        viewpager.currentItem = currentPosition2
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                currentPosition2 = position;
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state ==ViewPager.SCROLL_STATE_IDLE){
                    if (currentPosition2 == 0){
                        viewpager.setCurrentItem(data.size - 2, false);//切换，不要动画效果
                    }else if (currentPosition2 == data.size - 1){
                        viewpager.setCurrentItem(1, false);//切换，不要动画效果
                    }
                }
            }

        })




        joyRunBanner.setBannerLoadAdapter { joyRunBanner, data, itemView, position ->
            val imageView: ImageView = itemView.findViewById(R.id.image)
//            val mf = MultiTransformation(CenterCrop(), RoundedCorners(40))
            Glide.with(this@MainActivity).load(data)
                .into(imageView)

//            if (position == 1) {
//                imageView.visibility = View.GONE
//                val mediaPlayer = MediaPlayer()
//                var textureView = itemView.findViewById<TextureView>(R.id.video_view)
//                textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
//                    override fun onSurfaceTextureAvailable(
//                        surface: SurfaceTexture?,
//                        width: Int,
//                        height: Int
//                    ) {
//                        thread {
//                            mediaPlayer.setDataSource("https://frontend-app.thejoyrun.com/dev/home_banner.mp4")
//                            mediaPlayer.setSurface(Surface(surface))
//                            mediaPlayer.setOnPreparedListener {
//                                mediaPlayer.isLooping = true
//                                it.start()
//                            }
//                            mediaPlayer.prepare()
//                        }
//                    }
//
//                    override fun onSurfaceTextureSizeChanged(
//                        surface: SurfaceTexture?,
//                        width: Int,
//                        height: Int
//                    ) {
//                    }
//
//                    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
//                        textureView = null
//                        mediaPlayer.stop()
//                        mediaPlayer.release()
//                        return true
//                    }
//
//                    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
//                    }
//
//                }
//
//
////                val videoView = itemView.findViewById<VideoAdvertView>(R.id.video_view)
//////            videoView.setVideoPath(externalCacheDir?.absolutePath+ "/asd.mp4")
////                videoView.setVideoPath("https://frontend-app.thejoyrun.com/dev/home_banner.mp4")
////                videoView.start()
//////                videoView.setZOrderOnTop(true)
//////                videoView.setZOrderMediaOverlay(true)
////                videoView.setOnPreparedListener {
////                    it.isLooping = true
////                    imageView.visibility = View.GONE
////                }
//            }
        }

        joyRunBanner.setOnBannerItemClickListener { joyRunBanner, bannerItem, view, position ->
            Toast.makeText(this@MainActivity, position.toString() + bannerItem, Toast.LENGTH_SHORT)
                .show()
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
            startActivity(Intent(this, ScrollActivity::class.java))
//            joyRunBanner.getViewPager().startTimer()
        }

        btn_stop.setOnClickListener {
//            joyRunBanner.setScrollDuration(DecelerateInterpolator() , 600)
            joyRunBanner.viewPager.stopTimer()
        }



        btn_refresh.setOnClickListener {
            joyRunBanner.refreshBannerData(
                listOf(
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578397987333&di=49a0e58e716879f19562c57bbe26981a&imgtype=0&src=http%3A%2F%2Ft8.baidu.com%2Fit%2Fu%3D2247852322%2C986532796%26fm%3D79%26app%3D86%26f%3DJPEG%3Fw%3D1280%26h%3D853",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578397987331&di=706d56b72ad15ae5b0a6c49787e55335&imgtype=0&src=http%3A%2F%2Ft9.baidu.com%2Fit%2Fu%3D583874135%2C70653437%26fm%3D79%26app%3D86%26f%3DJPEG%3Fw%3D3607%26h%3D2408",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578398034097&di=4d7bdc1390087488326fae46167f2378&imgtype=0&src=http%3A%2F%2Ft8.baidu.com%2Fit%2Fu%3D2983382668%2C1612769941%26fm%3D79%26app%3D86%26f%3DJPEG%3Fw%3D1280%26h%3D854"
                )
            )
        }



//        joyRunBanner.initIndicator()
//            .setFocusResId(R.drawable.ic_dot_press)
//            .setNormalResId(R.drawable.ic_dot_normal)
//            .setIndicatorPadding(10)
//            .build()
        joyRunBanner.setScrollDuration(500)
//        joyRunBanner.setPageStyle(PageStyle.MULTI_PAGE , 16)
        joyRunBanner.setBannerData(R.layout.my_banner_item, data)
        joyRunBanner.setCurrentItem(currentPosition)
        joyRunBanner.viewPager.setOffscreenPageLimit(7)

        joyRunBanner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state ==ViewPager.SCROLL_STATE_IDLE){
                    if (currentPosition == 0){
                        joyRunBanner.setCurrentItem(data.size - 2, false)//切换，不要动画效果
                    }else if (currentPosition == data.size - 1){
                        joyRunBanner.setCurrentItem(1, false)//切换，不要动画效果
                    }
                }
            }

        })


//        joyRunBanner.setLoop(false)


        mulit_page.setOnClickListener {
            joyRunBanner.setScrollDuration(500)
            joyRunBanner.setPageStyle(PageStyle.MULTI_PAGE, 16)
            joyRunBanner.setBannerData(R.layout.my_banner_item, data)
        }

        mulit_page_scale.setOnClickListener {
            joyRunBanner.setScrollDuration(500)
            joyRunBanner.setPageStyle(PageStyle.MULTI_PAGE_SCALE)
            joyRunBanner.setBannerData(R.layout.my_banner_item, data)
        }

        mulit_maggin_page.setOnClickListener {
            joyRunBanner.setScrollDuration(500)
            joyRunBanner.setPageStyle(PageStyle.MARGIN_PAGE, 16)
            joyRunBanner.setBannerData(R.layout.my_banner_item, data)
        }

    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.e("asd", "onNewIntent")
    }

    override fun onStart() {
        super.onStart()
        Log.e("asd", "MainActivity onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("asd", "MainActivity onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.e("asd", "MainActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("asd", "MainActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e("asd", "MainActivity onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("asd", "MainActivity onDestroy")
    }


}
