package com.joyrun.bannersample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.jzvd.JZDataSource
import cn.jzvd.JZMediaSystem
import cn.jzvd.Jzvd
import com.bumptech.glide.Glide

/**
 * author: wenjie
 * date: 2022/9/22 22:17
 * description:
 */
class BannerAdapter2 : RecyclerView.Adapter<ViewHolder>() {

    private var mData = mutableListOf<String>()

    fun setNewData(data: MutableList<String>) {
        this.mData = data
        notifyDataSetChanged()
    }

    class ImageHolder(view: View) : ViewHolder(view) {
        val imageView = view.findViewById<ImageView>(R.id.iv_banner_cover)
    }

    class VideoHolder(view: View) : ViewHolder(view) {
        val imageView = view.findViewById<ImageView>(R.id.iv_banner_cover)
        val textureVideoView = view.findViewById<AdvertVideoView>(R.id.texture_view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mData[position].endsWith(".mp4")) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 1) {
            Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT)
            VideoHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_community_banner_video, parent, false)
            ).also {
                it.textureVideoView.setAllControlsVisiblity(View.GONE , View.GONE , View.GONE , View.GONE , View.VISIBLE , View.GONE , View.GONE )
            }
        } else {
            ImageHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_community_banner_topic, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == 1) {
            holder as VideoHolder
            Glide.with(holder.itemView)
                .load(mData[position])
                .into(holder.textureVideoView.posterImageView)
            val file = holder.imageView.context.cacheDir.absolutePath + "/274242176f184e882a818c10eb9aaaf.mp4"
//            holder.textureVideoView.setUp(file , "")
            holder.textureVideoView.setUp(JZDataSource(mData[position]) , Jzvd.SCREEN_NORMAL , JZMediaSystem::class.java)
            holder.textureVideoView.startVideo()
//            holder.textureVideoView.setDataSource(file)
//            holder.textureVideoView.setLooping(true)
//            holder.textureVideoView.setListener(object : TextureVideoView.MediaPlayerListener() {
//                override fun onVideoPrepared() {
//                    Log.e("asd" , "onVideoPrepared")
//                    holder.imageView.visibility = View.GONE
//                }
//            })
//            holder.textureVideoView.play()
        } else {
            holder as ImageHolder
            Glide.with(holder.itemView)
                .load(mData[position])
                .into(holder.imageView)
        }
    }
}