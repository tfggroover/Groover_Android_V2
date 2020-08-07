package com.amartindalonsoc.groover.utils

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import com.amartindalonsoc.groover.activities.LoginActivity
import com.amartindalonsoc.groover.activities.MainActivity

object Utils {

    fun startLoginActivity(activity: Activity) {
        val loginIntent = Intent(activity, LoginActivity::class.java)
        ContextCompat.startActivity(activity, loginIntent, null)
        activity.finish()
    }

    fun startMainActivity(activity: Activity) {
        val mainIntent = Intent(activity, MainActivity::class.java)
        ContextCompat.startActivity(activity, mainIntent, null)
        activity.finish()
    }
}