package io.touchgiftbox.util

import android.app.Activity

class util {
    fun setUserDataToSharedPreference(activity: Activity, touch: Int) {
        val setting  = activity.getSharedPreferences ("setting", Activity.MODE_PRIVATE)
        val editor = setting.edit()
        editor.putInt("touch", touch)

        editor.apply()
    }
}