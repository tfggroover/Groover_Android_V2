package com.amartindalonsoc.groover.activities

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.models.SpotifyRefresh
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.amartindalonsoc.groover.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreen : AppCompatActivity() {
    val splashActivity: Activity = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
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
                        Utils.startMainActivity(splashActivity)
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

}