package com.joyrun.bannersample

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

/**
 * author: wenjie
 * date: 2020-01-03 18:45
 * descption:
 */

class WelComeActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wel)

        Handler().postDelayed({
            startActivity(Intent(this , MainActivity::class.java))
            finish()
        },500)
    }

}