package com.joyrun.bannersample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joyrun.bannersample.databinding.Fragment1Binding

/**
 * author: wenjie
 * date: 2022/9/28 09:43
 * description:
 */
class Fragment1 : Fragment() {

    private lateinit var binding: Fragment1Binding
    private val bannerAdapter by lazy { BannerAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Fragment1Binding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.e("asd" , "1 onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("asd" , "1 onPause")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.banner.setAdapter(bannerAdapter)
        binding.tvText.setOnClickListener {
            startActivity(Intent(activity , ViewPager2Activity::class.java))
        }
//        lifecycle.addObserver(binding.banner)
        bannerAdapter.setNewData(mutableListOf(
            "https://joyrun-activity-upyun.thejoyrun.com/advert/0efad2703ffa4d37a43bbdc6de1cca8f.jpg",
            "https://joyrun-activity-upyun.thejoyrun.com/joyrun-activity/h5_demo/274242176f184e882a818c10eb9aaaf.mp4",
            "https://joyrun-activity-upyun.thejoyrun.com/advert/16e8b0ddcf054f91b183e4fe3e5493ff.jpg",
        ))
    }

}