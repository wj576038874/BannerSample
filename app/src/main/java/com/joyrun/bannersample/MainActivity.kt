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

        val s = """
            {"allcalorie":247142,"allmeter":3976,"allpo":14,"allsecond":3694,"birthday":"19940315","birthdayStr":"1994-03-15 00:00:00","cell":"86-15012551396","displayName":"可能否","faceurl":"http://linked-runner-upyun.thejoyrun.com/linked-runner/u_92838554_avatar_191116_2342116082.jpg","gender":1,"height":175,"id":0,"introduction":"","lastLoginInfoKeyIdV2":"","lastLoginType":"username","logtime":1577945688,"mail":"","maxContinuousWeeks":3,"name":"可能否","nameWithRemark":"可能否","nick":"可能否","pwd":"","qqToken":"","qqopenid":"","regtime":1571630947,"remark":"","runDays":4,"sid":"3bcb02d99ecc8faa33e26317aff0a3d3","uid":92838554,"userrunlevel":"J2","verContent":"","verType":0,"vip":false,"vipDrawableRes":0,"weiboToken":"","weibo_uid":"","weight":60,"weixinToken":"","weixinopenid":"","year":1994}
        """.trimIndent()
        jiami.setOnClickListener {

        }

        jiemi.setOnClickListener {
            startActivity(Intent(this , ScrollActivity::class.java))
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
//            joyRunBanner.getViewPager().startTimer()
        }

        btn_stop.setOnClickListener {
//            joyRunBanner.setScrollDuration(DecelerateInterpolator() , 600)
            joyRunBanner.getViewPager().stopTimer()
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



        joyRunBanner.initIndicator()
            .setFocusResId(R.drawable.ic_dot_press)
            .setNormalResId(R.drawable.ic_dot_normal)
            .setIndicatorPadding(10)
            .build()
        joyRunBanner.setScrollDuration(500)
        joyRunBanner.setPageStyle(PageStyle.MULTI_PAGE_SCALE )
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
