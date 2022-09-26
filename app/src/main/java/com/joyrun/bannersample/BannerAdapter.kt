package com.joyrun.bannersample

import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * author: wenjie
 * date: 2022/9/21 15:09
 * description:
 */
class BannerAdapter : BaseQuickAdapter<String ,BaseViewHolder>(R.layout.item_community_banner_topic) {

    override fun onViewHolderCreated(holder: BaseViewHolder, parent: ViewGroup, viewType: Int) {
//        holder.addOnClickListener(R.id.tv_text)
    }

    override fun convert(holder: BaseViewHolder, item: String, position: Int) {

        holder.itemView.setOnClickListener { setOnItemClick(it, position) }
        holder.childClickViewIds.forEach {
            holder.getView<View>(it)?.setOnClickListener {
                onItemChildClickListener?.onItemChildClick(this@BannerAdapter , it , position)
            }
        }

        Glide.with(mContext)
            .load(item)
            .apply(RequestOptions.bitmapTransform(MultiTransformation(CenterCrop(),RoundedCorners(16))))
            .into(holder.getView(R.id.iv_banner_cover))
    }


}