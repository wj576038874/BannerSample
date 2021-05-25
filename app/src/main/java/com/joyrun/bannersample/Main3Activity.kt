package com.joyrun.bannersample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_main2.*

/**
 * author: wenjie
 * date: 2020/9/22 10:41 AM
 * descption:
 */
class Main3Activity : AppCompatActivity() {

    private val adapter by lazy { ListAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        val bannerItemView = BannerItemView(this)
        adapter.addHeaderView(bannerItemView)
        val data = mutableListOf<String>()
        for (i in 1..100) {
            data.add("item$i")
        }
        adapter.setNewData(data)
        val banners = listOf(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665548&di=efe0920c74ffae46d80bf9302e0ff67c&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb151f8198618367aa7f3cc7424738bd4b31ce525.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=7f4e5dac527d55168d7f29d9aeaad01b&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F9a504fc2d5628535bdaac29e9aef76c6a6ef63c2.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=680286202b1d5e1423298e0cfb392568&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F810a19d8bc3eb1354c94a704ac1ea8d3fd1f4439.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665546&di=6d699d47ec867d1d135c5bdb4daacbcc&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F21a4462309f7905296a7578106f3d7ca7acbd5d0.jpg"
        )
        bannerItemView.setBanners(banners)
    }


    class ListAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.normal_holder) {

        override fun convert(holder: BaseViewHolder, item: String, position: Int) {
            holder.setText(R.id.tv, position.toString())
        }

    }
}