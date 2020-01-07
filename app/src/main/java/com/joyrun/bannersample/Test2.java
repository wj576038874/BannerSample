package com.joyrun.bannersample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * author: wenjie
 * date: 2019-12-26 10:59
 * descption:
 */
public class Test2 {
    public static void main(String[] args) {

        List<Advert> adverts = new ArrayList<>();
        adverts.add(
                new Advert(1, "111" , 0));
        adverts.add(
                new Advert(3, "333" ,0));
        adverts.add(
                new Advert(4, "444"  ,0));
        adverts.add(
                new Advert(2, "222" , 0));
        adverts.add(
                new Advert(10, "666",0));
        adverts.add(
                new Advert(5, "555",0));
        adverts.add(
                new Advert(10, "klkl",0));
        adverts.add(
                new Advert(10, "hjhj",0));
        adverts.add(
                new Advert(7, "fgfg",0));
        adverts.add(
                new Advert(13, "asdasd",0));



//        List<Advert> advertsB = new ArrayList<>();
//
//        for (Advert advert : adverts){
//            int randomIndex = new Random().nextInt(1000);
//            advert.setRandomIndex(randomIndex);
//            if (advert.getBusinessType() == 1 || advert.getBusinessType() == 2){
//                advertsB.add(advert);
//            }
//        }
//
//        if (advertsB.size() > 0){
//            //如果广告列表中存在【商务】和【变现】的广告，那么就按照随机数进行排序。
//            Collections.sort(adverts , new AdvertComparable2());
//        }else {
//            //如果不存在，那么就先根据权重倒序 权重一样就按照随机数排序
//            Collections.sort(adverts , new AdvertComparable());
//        }
//
//        for (Advert advert : adverts){
//            System.out.println(advert.toString());
//        }
//
//        String s = DESUtil.encrypt("joyrun111111" , "wenjie");
//        System.out.println(s);
//        String ss = DESUtil.decrypt("joyrun111111" , s);
//        System.out.println(ss);


        long w = 512;
        long total = 12460;
        System.out.println((w/total)*100);
        System.out.println((100-53)*100/1000);
    }
}
