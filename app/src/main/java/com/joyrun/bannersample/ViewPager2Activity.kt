package com.joyrun.bannersample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.joyrun.bannersample.databinding.ViewpaerActivityBinding

/**
 * author: wenjie
 * date: 2022/9/20 11:52
 * description:
 */
class ViewPager2Activity : AppCompatActivity() {

    private lateinit var binding: ViewpaerActivityBinding
    private val myAdapter by lazy { MyAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewpaerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager2.adapter = myAdapter

        myAdapter.setNewData(
            mutableListOf("https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg",
//            "https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg",
//            "https://alifei05.cfp.cn/creative/vcg/800/new/VCG41N1210205351.jpg",
//            "https://alifei01.cfp.cn/creative/vcg/veer/800water/veer-368621010.jpg"
        )
        )

        binding.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                Log.e("viewpager2" , "viewpager2 onPageSelected $position")
            }
        })

        binding.btnAdd.setOnClickListener {
            myAdapter.addData("https://alifei05.cfp.cn/creative/vcg/800/new/VCG41N1210205351.jpg")
        }

    }

    class MyAdapter : BaseQuickAdapter<String , BaseViewHolder>(R.layout.banner_layout){

        override fun convert(holder: BaseViewHolder, item: String, position: Int) {
            Glide.with(holder.itemView.context)
                .load(item)
                .into(holder.getView(R.id.cover))
        }

    }
}