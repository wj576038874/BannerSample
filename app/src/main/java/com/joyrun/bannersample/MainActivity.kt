package com.joyrun.bannersample

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.view.animation.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.joyrun.banner.JoyrunBanner
import com.joyrun.banner.R
import com.joyrun.bannersample.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var bannerAdapter2: BannerAdapter2
    private lateinit var bannerAdapter3: BannerAdapter3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bannerAdapter = BannerAdapter()
        bannerAdapter2 = BannerAdapter2()
        bannerAdapter3 = BannerAdapter3()
        val data = mutableListOf(
            "https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg",
            "https://joyrun-activity-upyun.thejoyrun.com/joyrun-activity/h5_demo/274242176f184e882a818c10eb9aaaf.mp4",
            "https://alifei05.cfp.cn/creative/vcg/800/new/VCG41N1210205351.jpg",
//            "https://joyrun-activity-upyun.thejoyrun.com/advert/16e8b0ddcf054f91b183e4fe3e5493ff.jpg",
//            "https://joyrun-activity-upyun.thejoyrun.com/advert/c373bc52acfa41ea97998471bfb4c504.jpg",
//            "https://joyrun-activity-upyun.thejoyrun.com/advert/fd93cff9f01243cb8d9a5c4641ba324e.png",
//            "https://joyrun-activity-upyun.thejoyrun.com/advert/4e48fe4e8a5342b48d6bb55cfdb78bdd.jpg"
        )
//        binding.banner.initIndicator()
//            .setFocusDrawableId(R.drawable.joyrun_banner_shape_indicator_selected)
//            .setNormalDrawableId(R.drawable.joyrun_banner_shape_indicator_normal)
//            .setNormalColor(Color.BLUE)
//            .setFocusColor(Color.RED)
////            .setIndicatorPadding(30)
//            .setRadius(6)
//            .setMargin(0,0,0,20)
//            .build()
//        binding.banner.setAdapter(bannerAdapter)
//        binding.banner.setEnableLoop(false)
//
        binding.banner.setAutoPlay(false)


//        binding.banner.initIndicator()
//            .setFocusDrawableId(R.drawable.selected)
//            .setNormalDrawableId(R.drawable.normal)
//            .setMargin(0, 0, 0, 16)
//            .setGravity(Gravity.CENTER or Gravity.BOTTOM)
//            .build()

        binding.banner.setAutoPlay(true)
            .setEnableLoop(true)
//            .disableIndicator()
//            .setPagerScrollDuration(2000)
//            .setUserInputEnabled(false)
            .addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.left = 16
                    outRect.right = 16
                }
            }).adapter = bannerAdapter

        bannerAdapter.setNewData(data)
        bannerAdapter2.setNewData(data)
        bannerAdapter3.setNewData(data)

        binding.btnStart.setOnClickListener {
            bannerAdapter.remove(0)
        }

        bannerAdapter.setOnItemClickListener { adapter, view, position ->
            val pos = binding.banner.getRealPosition(position, adapter.data.size)
            Log.e("asd", "click $position $pos")
        }

        bannerAdapter.setOnItemChildClickListener { adapter, view, position ->
            Log.e("asd", "child click $position")
        }

        binding.banner.setOnBannerSelectedListener {
            Log.e("asd", "page selected $it")
        }

//        binding.banner.setOffscreenPageLimit(10)
        binding.btnRefresh.setOnClickListener {
            bannerAdapter.setNewData(
                mutableListOf(
                    "https://joyrun-activity-upyun.thejoyrun.com/advert/0efad2703ffa4d37a43bbdc6de1cca8f.jpg",
                    "https://joyrun-activity-upyun.thejoyrun.com/joyrun-activity/h5_demo/274242176f184e882a818c10eb9aaaf.mp4",
                    "https://joyrun-activity-upyun.thejoyrun.com/advert/16e8b0ddcf054f91b183e4fe3e5493ff.jpg",
                )
            )
        }
        binding.btnStop.setOnClickListener {
            bannerAdapter.addData("https://joyrun-activity-upyun.thejoyrun.com/advert/4e48fe4e8a5342b48d6bb55cfdb78bdd.jpg")
        }


        binding.mulitPage.setOnClickListener {
            binding.banner.setCurrentItem(1, true)
        }

        binding.mulitPageScale.setOnClickListener {
            Toast.makeText(this, binding.banner.getCurrentItem().toString(), Toast.LENGTH_SHORT)
                .show()
            startActivity(Intent(this , SecondActivity::class.java))
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
