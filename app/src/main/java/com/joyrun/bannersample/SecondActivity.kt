package com.joyrun.bannersample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.joyrun.bannersample.databinding.ActivityMain2Binding

/**
 * author: wenjie
 * date: 2022/9/26 15:15
 * description:
 */
class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewpager2.adapter = Adapter(this)
        binding.viewpager2.offscreenPageLimit = 2
    }


    private class Adapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return if (position == 0) Fragment1() else Fragment2()
        }

    }
}