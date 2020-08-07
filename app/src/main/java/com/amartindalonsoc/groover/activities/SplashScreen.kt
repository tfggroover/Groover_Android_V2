package com.amartindalonsoc.groover.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.utils.Utils

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        Thread.sleep(2000) // Temporal, se pasa al login despues de cargar los datos necesarios
        Utils.startLoginActivity(this)
    }
}