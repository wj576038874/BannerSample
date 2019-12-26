package com.joyrun.bannersample;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.joyrun.banner.JoyRunBanner;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;

/**
 * author: wenjie
 * date: 2019-12-19 17:03
 * descption:
 */
public class Main extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JoyRunBanner<String> joyRunBanner = findViewById(R.id.JoyRunBanner);
//        joyRunBanner.setBannerLoadAdapter(new Function4<JoyRunBanner, Object, View, Integer, Unit>() {
//            @Override
//            public Unit invoke(JoyRunBanner joyRunBanner, Object o, View view, Integer integer) {
//                Glide.with(getApplicationContext()).load(o).into((ImageView)view);
//                return null;
//            }
//        });

        joyRunBanner.setBannerLoadAdapter(new Function4<JoyRunBanner<String>, String, View, Integer, Unit>() {
            @Override
            public Unit invoke(JoyRunBanner<String> stringJoyRunBanner, String s, View view, Integer integer) {
                Glide.with(getApplicationContext()).load(s).into((ImageView)view);
                return null;
            }
        });

        List<String> dartas = new ArrayList<>();
        dartas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665548&di=efe0920c74ffae46d80bf9302e0ff67c&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb151f8198618367aa7f3cc7424738bd4b31ce525.jpg");
        dartas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=7f4e5dac527d55168d7f29d9aeaad01b&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F9a504fc2d5628535bdaac29e9aef76c6a6ef63c2.jpg");
        dartas.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=680286202b1d5e1423298e0cfb392568&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F810a19d8bc3eb1354c94a704ac1ea8d3fd1f4439.jpg");
        joyRunBanner.setBannerData(dartas);

    }


}
