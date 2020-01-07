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
        Advert(1, "111", 0),
        Advert(3, "333", 1),
        Advert(4, "444", 2),
        Advert(2, "222", 1),
        Advert(6, "666", 0),
        Advert(5, "555", 0)
    )


    val list2 = list.sortedWith(AdvertComparable())
    println(list2)

}

class AdvertComparable : Comparator<Advert> {

    override fun compare(o1: Advert, o2: Advert): Int {
        return when {
            o1.id > o2.id -> {
                -1
            }
            o1.id == o2.id -> {
                return if (o1.randomIndex > o2.randomIndex) -1 else 1
            }
            else -> {
                1
            }
        }

    }

}

class AdvertComparable2 : Comparator<Advert> {


    override fun compare(o1: Advert, o2: Advert): Int {
        return when {
            o1.randomIndex > o2.randomIndex -> {
                -1
            }
            o1.randomIndex == o2.randomIndex -> {
                0
            }
            else -> {
                1
            }
        }
    }
}


data class Advert(val id: Int, val title: String, val businessType: Int) {
    var randomIndex: Int = 0

    override fun toString(): String {
        return "Advert(id=$id, title=$title, businessType=$businessType , randomIndex = $randomIndex)"
    }
}