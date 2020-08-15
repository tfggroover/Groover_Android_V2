package com.amartindalonsoc.groover.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.responses.Place
import com.amartindalonsoc.groover.responses.SpotifyCallback
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.amartindalonsoc.groover.utils.Utils
import com.google.android.gms.maps.model.LatLng
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import retrofit2.Callback
import okhttp3.Cookie
import retrofit2.Call
import retrofit2.Response
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val stateRandom = UUID.randomUUID().toString()
    private val request_code_original = 1234 // Revisar a ver si esto tiene que ir cambiando
    val loginActivity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        verifyPermissions()
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
                    val request = Api.azureApiRequest()
                    val call = request.callbackSpotify(response.code, stateRandom, galletita.toString())
                    call.enqueue(object : Callback<SpotifyCallback>{

                        override fun onResponse(call: Call<SpotifyCallback>, response: Response<SpotifyCallback>) {
                            Log.i("newlogintest",response.message())
                            Log.i("newlogintest",response.body().toString())
                            if (response.isSuccessful) {
                                if (response.body() != null) {
                                    //TODO Guardar datos y mandar a getPlaces
                                    SharedPreferencesManager.saveUserFromCallback(response.body()!!, loginActivity)
                                    Log.i("newlogintest_saveddata", SharedPreferencesManager.getString(Constants.user_name, loginActivity)!!)
                                    Log.i("newlogintest_saveddata", SharedPreferencesManager.getString(Constants.user_email, loginActivity)!!)
                                    getPlaces()
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

    fun getPlaces() { // TODO Volver a pedir las places en profile si se modifica la distancia, mirar como hacer para descargar todo lo que esté en la misma ciudad y luego mostrar simplemente en función de la distancia
        val currentLocation = getLocation()
        SharedPreferencesManager.saveCameraLocation(currentLocation, this)
        SharedPreferencesManager.saveMapZoom(13.25f, this)
//        var savedDistance = SharedPreferencesManager.getFloat(Constants.selected_distance_to_place, this)!!.toDouble()
//        if (savedDistance < 0) {
//            savedDistance = 1000.0
//            SharedPreferencesManager.saveFloat(Constants.selected_distance_to_place, savedDistance.toFloat(), loginActivity)
//        }
        val request = Api.azureApiRequest()
        val call = request.getPlaces(currentLocation.latitude,currentLocation.longitude,4882.0,1,25)
        call.enqueue(object : Callback<List<Place>>{

            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                Log.i("getPlaces",response.message())
                Log.i("getPlaces",response.body().toString())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        //TODO Guardar datos y mandar a MainActivity
                        SharedPreferencesManager.savePlacesFromCallback(response.body()!!, loginActivity)

//                        val places_ids = SharedPreferencesManager.getPlacesIds(loginActivity)
//                        if (places_ids != null) {
//                            for (placeId in places_ids) {
//                                Log.i("getPlaces_saveddata", placeId)
//                                Log.i("getPlaces_saveddata", SharedPreferencesManager.getString(placeId.plus(Constants.places_display_name), loginActivity)!!)
//                                Log.i("getPlaces_saveddata", SharedPreferencesManager.getFloat(placeId.plus(Constants.places_latitude), loginActivity)!!.toString())
//                                Log.i("getPlaces_saveddata", SharedPreferencesManager.getFloat(placeId.plus(Constants.places_longitude), loginActivity)!!.toString())
//                            }
//                        }
//
                        Utils.startMainActivity(loginActivity)
                    }
                }
            }

            override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                Log.i("CallbackFailurePlaces", t.message)
            }

        })
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

    // Location
    @SuppressLint("MissingPermission")
    fun getLocation(): LatLng {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        var locationGps: Location? = null
        if (hasGps) {
            Log.d("CodeAndroidLocation", "hasGps")
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object :
                LocationListener {
                override fun onLocationChanged(location: Location?) {
                    if (location != null) {
                        locationGps = location
                        Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                        Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
                    }
                }
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                }
                override fun onProviderEnabled(provider: String?) {
                }
                override fun onProviderDisabled(provider: String?) {
                }
            })

            val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (localGpsLocation != null)
                locationGps = localGpsLocation
        }

        if(locationGps!= null){
            return LatLng(locationGps!!.latitude,locationGps!!.longitude)
        }

        return LatLng(0.0,0.0)
    }
}