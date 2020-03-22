package io.touchgiftbox.util

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var cnt: String? = ""
)