package io.touchgiftbox.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import io.touchgiftbox.R
import io.touchgiftbox.databinding.ActivitySplashBinding

class SplashActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@SplashActivity, R.layout.activity_splash)
        binding.activity = this@SplashActivity

        Glide.with(this@SplashActivity).asGif().load(R.drawable.giphy).into(binding.ivSplash)

        val delayHandler = Handler()
        delayHandler.postDelayed(this::doLaunchApp, 3000)
    }
    private fun doLaunchApp() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
    }
}