package com.amartindalonsoc.groover.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.models.SpotifyLoginCallback
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.amartindalonsoc.groover.utils.Utils
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import okhttp3.Cookie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val stateRandom = UUID.randomUUID().toString()
    private val request_code_original = 1234 // Revisar a ver si esto tiene que ir cambiando
    val loginActivity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        verifyPermissions()
        auth = Firebase.auth

        val loginButton = findViewById<ImageView>(R.id.login_button)
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
                    val request = Api.azureApiRequest()
                    val call = request.loginCallbackSpotify(response.code, stateRandom, galletita.toString())
                    makeCall(call, 0)

                }
                AuthorizationResponse.Type.ERROR -> println("Error en onActivityResult - No ha devuelto token" + response.error)
            }
        }
    }

    fun makeCall(callToMake: Call<SpotifyLoginCallback>, timesTried: Int) {
        callToMake.enqueue(object : Callback<SpotifyLoginCallback>{

            override fun onResponse(call: Call<SpotifyLoginCallback>, response: Response<SpotifyLoginCallback>) {
                Log.i("newlogintest",response.message())
                Log.i("newlogintest",response.body().toString())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        SharedPreferencesManager.saveUserFromCallback(response.body()!!, loginActivity)
                        Log.i("newlogintest_saveddata", SharedPreferencesManager.getString(Constants.user_name, loginActivity)!!)
                        Log.i("newlogintest_saveddata", SharedPreferencesManager.getString(Constants.user_email, loginActivity)!!)
                        firebaseLogin()

                    }
                }
            }

            override fun onFailure(call: Call<SpotifyLoginCallback>, t: Throwable) {
                Log.i("CallbackFailure", t.message)
                Log.i("CallbackFailure", "Tried $timesTried times")
                if (timesTried < 5) {
                    makeCall(callToMake.clone(), timesTried+1)
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
                            Utils.startMainActivity(loginActivity)
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