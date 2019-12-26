package com.joyrun.bannersample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.joyrun.banner.JoyRunBanner

class MyAdapter(private val mData:List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            0 -> {
                BannerHolder(LayoutInflater.from(parent.context).inflate(R.layout.banner_holder, parent , false))
            }
            else -> {
                NormalHolder(LayoutInflater.from(parent.context).inflate(R.layout.normal_holder, parent , false))
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            0 -> {
                val bannerHolder = holder as BannerHolder
                val bannerItem = mData[position] as Item.BannerItem
                bannerHolder.banner.setBannerLoadAdapter { joyRunBanner, url, view, position ->
                    Glide.with(bannerHolder.itemView.context).load(url).into(view as ImageView)
                }
                bannerHolder.banner.setScrollDuration(600)
                bannerHolder.banner.setBannerData(bannerItem.datas)
            }
            else -> {
                val normalItem = mData[position] as Item.NormalItem
                val normalHolder = holder as NormalHolder
                normalHolder.text.text = normalItem.text
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when(mData[position]){
            is Item.BannerItem -> 0
            is Item.NormalItem -> 1
        }
    }


    class BannerHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val banner:JoyRunBanner<String> = itemView.findViewById(R.id.banner)
    }


    class NormalHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val text:TextView = itemView.findViewById(R.id.tv)
    }

}