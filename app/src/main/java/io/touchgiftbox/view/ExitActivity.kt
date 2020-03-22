package io.touchgiftbox.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds

import io.touchgiftbox.R
import io.touchgiftbox.databinding.ActivityExitBinding


class ExitActivity: AppCompatActivity() {
    private lateinit var binding: ActivityExitBinding
    private lateinit var interstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@ExitActivity, R.layout.activity_exit)
        binding.activity = this@ExitActivity

        MobileAds.initialize(
            this,
            resources.getString(R.string.ad_mob_id)
        )
        interstitialAd = InterstitialAd(this)
        interstitialAd.adUnitId = getString(R.string.ad_mob_exit_id)
        interstitialAd.loadAd(AdRequest.Builder().build())

        interstitialAd.loadAd(
            AdRequest.Builder()
                .build()
        )
        interstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                interstitialAd.show()
            }

            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)
            }

            override fun onAdClosed() {
                super.onAdClosed()
                this@ExitActivity.finish()
            }
        }
    }
}