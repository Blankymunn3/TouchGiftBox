package io.touchgiftbox.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import io.touchgiftbox.R
import io.touchgiftbox.databinding.ActivityMainBinding
import io.touchgiftbox.util.User


class MainActivity : AppCompatActivity() {
    val TAG = this@MainActivity::class.java.simpleName

    private infix fun String.or(that: String): String = that
    private lateinit var binding: ActivityMainBinding
    private val database = FirebaseDatabase.getInstance().reference
    var token: String? = null

    lateinit var user: User
    lateinit var cnt: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binding.activity = this@MainActivity
        adMob()


        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            token = task.result?.token

            successToken(token)
        })

        binding.lottieGiftBox.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

            }
        })
    }


    private fun successToken(token: String?) {
        database.child("touch").orderByKey().equalTo(token)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    user = p0.getValue(User::class.java)!!
                    cnt = user.cnt.toString()
                    binding.txtCountTouch.text = cnt
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    user = p0.getValue(User::class.java)!!
                    cnt = user.cnt.toString()
                    binding.txtCountTouch.text = cnt
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    user = p0.getValue(User::class.java)!!
                    cnt = user.cnt.toString()
                    binding.txtCountTouch.text = cnt
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    user = p0.getValue(User::class.java)!!
                    cnt = user.cnt.toString()
                    binding.txtCountTouch.text = cnt
                }

            })
    }

    private fun adMob() {
        MobileAds.initialize(
            this,
            resources.getString(R.string.ad_mob_id)
        )
        val extras = Bundle()
        extras.putString("max_ad_content_rating", "G")
        val adRequest: AdRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()
        binding.adView.loadAd(adRequest)

        binding.adView.adListener = object  : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.e("adBanner", "onAdLoaded")
            }

            override fun onAdFailedToLoad(errorCode : Int) {
                // Code to be executed when an ad request fails.
                Log.e("adBanner", "onAdFailedToLoad")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.e("adBanner", "onAdOpened")
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.e("adBanner", "onAdClicked")
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.e("adBanner", "onAdLeftApplication")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.e("adBanner", "onAdClosed")
            }
        }
    }

    fun onClickGiftBox(view: View) {
        val to = if (token.isNullOrEmpty()) {
            "tetete"
        } else {
            token as String
        }

        var count: Int = binding.txtCountTouch.text.toString().toInt()
        count = --count

        var testRef = database.child("touch")
        var user = User((count).toString())
        testRef.child(to).setValue(user)
        if (count == 0) {
            binding.lottieGiftBox.isClickable = false
            binding.lottieGiftBox.isFocusable = false
            binding.lottieGiftBox.playAnimation()

        }

        binding.lottieGiftBox.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                binding.txtCountTouch.text = resources.getText(R.string.str_billion)
                count = binding.txtCountTouch.text.toString().toInt()
                testRef = database.child("touch")
                user = User((count).toString())
                testRef.child(to).setValue(user)
                binding.lottieGiftBox.isClickable = true
                binding.lottieGiftBox.isFocusable = true
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

        })
    }

    public override fun onPause() {
        binding.adView.pause()
        super.onPause()
    }

    public override fun onResume() {
        super.onResume()
        binding.adView.resume()
    }

    public override fun onDestroy() {
        binding.adView.destroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        startActivity(Intent(this@MainActivity, ExitActivity::class.java))
        finish()
    }
}
