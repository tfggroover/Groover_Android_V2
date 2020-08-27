package com.amartindalonsoc.groover.activities

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.models.ItemForRecommendation
import com.amartindalonsoc.groover.models.Place
import com.amartindalonsoc.groover.ui.main.*
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.google.android.gms.maps.model.LatLng
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var placesList: List<Place>
    lateinit var recommendedPlacesList: List<Place>
    lateinit var centerCoords: LatLng
    lateinit var userToken: String
    lateinit var spotifyAppRemote: SpotifyAppRemote
    var itemForRecommendation: ItemForRecommendation? = null
    var selectedItem = -1
    var distance: Double = 100.0
    var zoom: Float = 13.25f
    var spotifyAccountType = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPlaces()

        userToken = SharedPreferencesManager.getString(Constants.spotify_user_token, this)!!
        spotifyAccountType = SharedPreferencesManager.getString(Constants.spotify_account_type, this)!!

        SpotifyAppRemote.connect(this, ConnectionParams.Builder(getString(R.string.client_id)).setRedirectUri(getString(R.string.redirectUri)).showAuthView(true).build(), object: Connector.ConnectionListener{
            override fun onFailure(p0: Throwable) {
            }

            override fun onConnected(p0: SpotifyAppRemote) {
                spotifyAppRemote = p0
            }

        })

        navigation.setOnNavigationItemSelectedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            when (it.itemId) {
                R.id.navigation_home -> {
                    val shouldLoad = (currentFragment !is RecognizerFragment)
                    if (shouldLoad) {
                        val fragment = RecognizerFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                    }
                    shouldLoad
                }
                R.id.navigation_map -> {
                    val shouldLoad = (currentFragment !is MapFragment)
                    if (shouldLoad) {
                        val fragment = MapFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                    }
                    shouldLoad
                }
                R.id.navigation_recommendation -> {
                    val shouldLoad = (currentFragment !is RecommendationFragment)
                    if (shouldLoad) {
                        val fragment = RecommendationFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                    }
                    shouldLoad
                }
                R.id.navigation_profile -> {
                    val shouldLoad = (currentFragment !is ProfileFragment)
                    if (shouldLoad) {
                        val fragment = ProfileFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                    }
                    shouldLoad
                }
                else -> false
            }
        }


    }

    fun getPlaces() {
        val currentLocation = getLocation()
        centerCoords = currentLocation
//        SharedPreferencesManager.saveCameraLocation(currentLocation, this)
//        SharedPreferencesManager.saveMapZoom(13.25f, this)

        val request = Api.azureApiRequest()
        val call = request.getPlaces(currentLocation.latitude,currentLocation.longitude,4882.0,1,25)
        call.enqueue(object : Callback<List<Place>> {

            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                Log.i("getPlaces",response.message())
                Log.i("getPlaces",response.body().toString())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        placesList = response.body()!!
                        navigation.visibility = View.VISIBLE
                        val fragment = MapFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
                    }
                }
            }

            override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                Log.i("CallbackFailurePlaces", t.message)
            }

        })
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

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment is ProfileFragment) {
            Log.i("BACK_CONTROL", "is profile")
        }
        if (currentFragment is MapFragment) {
            Log.i("BACK_CONTROL", "is map")
        }
        if (currentFragment is RecognizerFragment) {
            Log.i("BACK_CONTROL", "is recon")
        }
        if (currentFragment is RecommendationFragment) {
            Log.i("BACK_CONTROL", "is recommendation")
        }
        if (currentFragment is PlaylistDetailsFragment) {
            Log.i("BACK_CONTROL", "is details")
            val fragment = RecommendationFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        }
    }

}