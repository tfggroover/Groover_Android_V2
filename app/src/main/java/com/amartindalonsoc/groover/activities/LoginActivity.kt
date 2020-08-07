package com.amartindalonsoc.groover.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.responses.SpotifyCallback
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.amartindalonsoc.groover.utils.Utils
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import retrofit2.Callback
import okhttp3.Cookie
import okhttp3.Request
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private val stateRandom = UUID.randomUUID().toString()
    private val request_code_original = 1234 // Revisar a ver si esto tiene que ir cambiando
    val loginActivity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            val builder = AuthorizationRequest.Builder(getString(R.string.client_id), AuthorizationResponse.Type.CODE, getString(R.string.redirectUri))
            builder.setScopes(arrayOf("app-remote-control", "user-read-email", "user-read-recently-played", "playlist-read-private", "streaming", "user-read-private","user-modify-playback-state","user-top-read"))
            builder.setState(stateRandom)
            val request = builder.build()
            AuthorizationClient.openLoginActivity(loginActivity, request_code_original, request)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == request_code_original){
            val response = AuthorizationClient.getResponse(resultCode,intent)
            when(response.type){

                AuthorizationResponse.Type.CODE -> {
                    val galletita = Cookie.Builder().domain("tfggroover.azurewebsites.net").name("State").value(stateRandom).secure().httpOnly().build()
                    val request = Api.getSpotifyUserJSONfromCallback()
                    val call = request.callbackSpotify(response.code, stateRandom, galletita.toString())
                    call.enqueue(object : Callback<SpotifyCallback>{

                        override fun onResponse(call: Call<SpotifyCallback>, response: Response<SpotifyCallback>) {
                            Log.i("newlogintest",response.message())
                            Log.i("newlogintest",response.body().toString())
                            if (response.isSuccessful) {
                                if (response.body() != null) {
                                    SharedPreferencesManager.saveUserFromCallback(response.body()!!, loginActivity)
                                    //TODO Guardar datos y mandar a MainActivity
                                    Log.i("newlogintest_saveddata", SharedPreferencesManager.getString(Constants.user_name, loginActivity)!!)
                                    Log.i("newlogintest_saveddata", SharedPreferencesManager.getString(Constants.user_email, loginActivity)!!)
                                    Utils.startMainActivity(loginActivity)
                                }
                            }
                        }

                        override fun onFailure(call: Call<SpotifyCallback>, t: Throwable) {
                            Log.i("CallbackFailure", t.message)
                        }

                    })

                }
                AuthorizationResponse.Type.ERROR -> println("Error en onActivityResult - No ha devuelto token" + response.error)
            }
        }
    }
}