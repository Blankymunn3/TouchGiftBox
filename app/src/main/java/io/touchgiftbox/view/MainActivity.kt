package io.touchgiftbox.view

import android.R.attr.button
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.*
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
        database.child("touch").orderByKey().equalTo(token).addChildEventListener(object: ChildEventListener {
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
            getString(R.string.ad_mob_unit_id_test) or getString(R.string.ad_mob_unit_id)
        )
        val extras = Bundle()
        extras.putString("max_ad_content_rating", "G")
        val adRequest: AdRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()
        binding.adView.loadAd(adRequest)
    }

    fun onClickGiftBox(view: View) {
        var to = ""
        if (token.isNullOrEmpty()) {
            to = "tetete"
        } else {
            to = token as String
        }

        var count: Int = binding.txtCountTouch.text.toString().toInt()

        val testRef = database.child("touch")
        val user = User((--count).toString())

        testRef.child(to).setValue(user)
        if (count == 0) {
            binding.lottieGiftBox.playAnimation()
        }
    }

    public override fun onPause() {
        binding.adView.pause()
        super.onPause()
    }

    // Called when returning to the activity
    public override fun onResume() {
        super.onResume()
        binding.adView.resume()
    }

    // Called before the activity is destroyed
    public override fun onDestroy() {
        binding.adView.destroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}
