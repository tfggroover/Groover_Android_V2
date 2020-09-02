package com.amartindalonsoc.groover.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.models.SpotifyRefresh
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.amartindalonsoc.groover.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val splashActivity: Activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        verifyPermissions()
        auth = Firebase.auth
//        Thread.sleep(2000) // Temporal, se pasa al login despues de cargar los datos necesarios

        val refresh_token = SharedPreferencesManager.getString(Constants.spotify_refresh_token,splashActivity)
        if (refresh_token != null){
            Log.i("REFRESH", "not null")
            Log.i("REFRESH", refresh_token)

            val request = Api.azureApiRequest()
            val call = request.refreshLogin(refresh_token)
            makeCall(call, 0)
        } else {
            Log.i("REFRESH", "null")
            Utils.startLoginActivity(this)
        }
    }

    fun makeCall(callToMake: Call<SpotifyRefresh>, timesTried: Int) {
        callToMake.enqueue(object : Callback<SpotifyRefresh> {

            override fun onResponse(call: Call<SpotifyRefresh>, response: Response<SpotifyRefresh>) {
                Log.i("newlogintest",response.message())
                Log.i("newlogintest",response.body().toString())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        //TODO Guardar datos y mandar a getPlaces
                        SharedPreferencesManager.saveUserFromRefresh(response.body()!!, splashActivity)
                        Log.i("newlogintest_saveddata", SharedPreferencesManager.getString(Constants.user_name, splashActivity)!!)
                        Log.i("newlogintest_saveddata", SharedPreferencesManager.getString(Constants.user_email, splashActivity)!!)

                        firebaseLogin()
                    }
                }
            }

            override fun onFailure(call: Call<SpotifyRefresh>, t: Throwable) {
                Log.i("CallbackFailure", t.message)
                Log.i("CallbackFailure", "Tried $timesTried times")
                if (timesTried < 5) {
                    makeCall(callToMake.clone(), timesTried+1)
                    //TODO Comprobar que esto funciona bien
                } else {
                    Utils.startLoginActivity(splashActivity)
                }
            }

        })
    }

    fun firebaseLogin() {
        auth.signInWithCustomToken(SharedPreferencesManager.getString(Constants.firebase_token, this)!!).addOnCompleteListener {
            if (it.isSuccessful) {
                auth.currentUser!!.getIdToken(true).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result != null && task.result!!.token != null) {
                            Log.i("BEARER", "_" + task.result!!.token!!)
                            SharedPreferencesManager.saveFirebaseBearer(task.result!!.token!!, this)
                            Utils.startMainActivity(splashActivity)
                        }
                    } else {
                        Log.i("BEARER", it.result.toString())
                    }
                }
            } else {
                Log.i("BEARER", it.result.toString())
            }
        }
    }


    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS = arrayOf<String>(
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    fun verifyPermissions() {
        for (i in PERMISSIONS.indices) {
            val permission = ActivityCompat.checkSelfPermission(this, PERMISSIONS[i])
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, PERMISSIONS,
                    REQUEST_EXTERNAL_STORAGE
                )
                break
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        for (i in permissions.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED && (permissions[i] == Manifest.permission.RECORD_AUDIO || permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (shouldShowRequestPermissionRationale(permissions[i])) {
                    verifyPermissions()
                } else {
                    val closeAppPopup = AlertDialog.Builder(this)
                    closeAppPopup.setTitle("Permissions required")
                    closeAppPopup.setMessage("Location and microphone permissions are required to use the app. Enable them in Settings and restart the app.")
                    closeAppPopup.setPositiveButton("Close"){ _, _ ->
                        finish()
                    }
                    closeAppPopup.show()
                }
                break
            }
        }
    }


}