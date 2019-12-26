package com.joyrun.bannersample

import java.lang.reflect.Array
import java.util.*

/**
 * author: wenjie
 * date: 2019-12-26 10:44
 * descption:
 */

fun main() {

    val list = listOf(
        Advert(1, "111"),
        Advert(3, "333"),
        Advert(4, "444"),
        Advert(2, "222"),
        Advert(6, "666"),
        Advert(5, "555")
    )


    val list2 = list.sortedWith(AdvertComparable())
    println(list2)

}

class AdvertComparable : Comparator<Advert>{


    override fun compare(o1: Advert, o2: Advert): Int {
        return when {
            o1.id > o2.id -> {
                1
            }
            o1.id == o2.id -> {
                0
            }
            else -> {
                -1
            }
        }

    }

}


data class Advert(val id: Int, val title: String)