package com.amartindalonsoc.groover.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.ui.main.MapFragment
import com.amartindalonsoc.groover.ui.main.RecognizerFragment
import com.amartindalonsoc.groover.ui.main.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    val fragment = RecognizerFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                    true
                }
                R.id.navigation_map -> {
                    val fragment = MapFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                    false
                }
                R.id.navigation_recommendation -> {
                    false
                }
                R.id.navigation_profile -> {
                    val fragment = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                    true
                }
                else -> false
            }
        }


        val fragment = ProfileFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }


}