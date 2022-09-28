package com.joyrun.bannersample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.joyrun.bannersample.databinding.Fragment1Binding
import com.joyrun.bannersample.databinding.Fragment2Binding

/**
 * author: wenjie
 * date: 2022/9/28 09:43
 * description:
 */
class Fragment2 : Fragment() {

    private lateinit var binding: Fragment2Binding
    private val bannerAdapter by lazy { BannerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Fragment2Binding.inflate(inflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.e("asd" , "2 onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e("asd" , "2 onPause")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.banner.setAdapter(bannerAdapter)
        lifecycle.addObserver(binding.banner)
        bannerAdapter.setNewData(
            mutableListOf(
                "https://alifei05.cfp.cn/creative/vcg/800/new/VCG41N1210205351.jpg",
                "https://alifei01.cfp.cn/creative/vcg/veer/800water/veer-368621010.jpg"
            )
        )
    }

}