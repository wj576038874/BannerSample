package com.joyrun.bannersample;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.joyrun.banner.JoyRunBanner;
import com.joyrun.banner.viewpager.TimerHandler;
import com.joyrun.banner.viewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function4;

/**
 * author: wenjie
 * date: 2020-01-06 10:56
 * descption:
 */
public class ScrollActivity extends AppCompatActivity {

    private JoyRunBanner<String> joyRunBanner;

    private NestedScrollView scrollView;

    private TimerHandler timer;

    private ImageView imageView;
    private ImageView imageView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_scrolling);

        joyRunBanner = findViewById(R.id.banner);
        imageView = findViewById(R.id.imageview);
        scrollView = findViewById(R.id.scrollView);
        imageView2 = findViewById(R.id.imageview2);
        Glide.with(this)
                .load("https://joyrun-activity-upyun.thejoyrun.com/advert/e8e3a3a9b1a64dc08875c779bc4aefe2.gif")
                .into(imageView);

        Glide.with(this)
                .load("https://joyrun-activity-upyun.thejoyrun.com/advert/bca35e01de9c44dc89d88e7315a904e4.png")
                .into(imageView2);

        timer = new TimerHandler(mTimerHandlerListener, 3000);
        startTimer();

        joyRunBanner.setBannerLoadAdapter(new Function4<JoyRunBanner<String>, String, View, Integer, Unit>() {
            @Override
            public Unit invoke(JoyRunBanner<String> stringJoyRunBanner, String s, View itemView, Integer integer) {
                ImageView imageView = itemView.findViewById(R.id.image);
                MultiTransformation mf = new MultiTransformation<>(new CenterCrop(), new RoundedCorners(40));
                Glide.with(ScrollActivity.this).load(s).apply(RequestOptions.bitmapTransform(mf)).into(imageView);
                return Unit.INSTANCE;
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                boolean bol = isVisibleLocal(joyRunBanner);
                if (bol){
                    startTimer();
                }else {
                    stopTimer();
                }
//                Log.e("asd", bol + "");
            }
        });

        List<String> data = new ArrayList<>();

        data.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665548&di=efe0920c74ffae46d80bf9302e0ff67c&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2Fb151f8198618367aa7f3cc7424738bd4b31ce525.jpg");
        data.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=7f4e5dac527d55168d7f29d9aeaad01b&imgtype=0&src=http%3A%2F%2Fa.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F9a504fc2d5628535bdaac29e9aef76c6a6ef63c2.jpg");
        data.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665547&di=680286202b1d5e1423298e0cfb392568&imgtype=0&src=http%3A%2F%2Fh.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F810a19d8bc3eb1354c94a704ac1ea8d3fd1f4439.jpg");
        data.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1576754665546&di=6d699d47ec867d1d135c5bdb4daacbcc&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F21a4462309f7905296a7578106f3d7ca7acbd5d0.jpg");

        joyRunBanner.initIndicator()
                .setFocusResId(R.drawable.ic_dot_press)
                .setNormalResId(R.drawable.ic_dot_normal)
                .setIndicatorPadding(10)
                .build();
        joyRunBanner.setScrollDuration(500);
        joyRunBanner.setBannerData(R.layout.my_banner_item,
                data
        );

        joyRunBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("asd", "onPageSelected" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private boolean isVisibleLocal(View target) {
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        return rect.top >= 0;
    }


    private TimerHandler.TimerHandlerListener mTimerHandlerListener = new TimerHandler.TimerHandlerListener() {

        @Override
        public void callBack() {
            joyRunBanner.getViewPager().scrollNextPage();
        }
    };

    public void startTimer() {
        if (timer == null || !timer.isStopped) {
            return;
        }
        timer.listener = mTimerHandlerListener;
        timer.removeCallbacksAndMessages(null);
        timer.tick();
        timer.isStopped = false;
    }

    public void stopTimer() {
        if (timer == null || timer.isStopped) {
            return;
        }
        timer.removeCallbacksAndMessages(null);
        timer.listener = null;
        timer.isStopped = true;
    }


    public static class TimerHandler extends Handler {

        public interface TimerHandlerListener {
            void callBack();
        }
        long interval;
        boolean isStopped = true;
        TimerHandlerListener listener;

        static final int MSG_TIMER_ID = 87108;

        TimerHandler(TimerHandlerListener listener, long interval) {
            this.listener = listener;
            this.interval = interval;
        }

        @Override
        public void handleMessage(Message msg) {
            if (MSG_TIMER_ID == msg.what) {
                if (listener != null) {
                    listener.callBack();
                    tick();
                }
            }
        }

        void tick() {
            sendEmptyMessageDelayed(TimerHandler.MSG_TIMER_ID, interval);
        }
    }

}
