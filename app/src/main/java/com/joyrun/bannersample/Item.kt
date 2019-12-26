package com.joyrun.bannersample

/**
 * author: wenjie
 * date: 2019-12-23 11:51
 * descption:
 */
sealed class Item {
    data class BannerItem(val datas:List<String>):Item()
    data class NormalItem(val text:String):Item()
}