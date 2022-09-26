package com.joyrun.bannersample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }
}