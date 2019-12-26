package com.joyrun.bannersample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * author: wenjie
 * date: 2019-12-26 10:59
 * descption:
 */
public class Test2 {
    public static void main(String[] args) {

        List<Advert> adverts = new ArrayList<>();
        adverts.add(
                new Advert(1, "111"));
        adverts.add(
                new Advert(3, "333"));
        adverts.add(
                new Advert(4, "444"));
        adverts.add(
                new Advert(2, "222"));
        adverts.add(
                new Advert(6, "666"));
        adverts.add(
                new Advert(5, "555"));


        Collections.sort(adverts , new AdvertComparable());

        System.out.println(adverts);
    }
}
