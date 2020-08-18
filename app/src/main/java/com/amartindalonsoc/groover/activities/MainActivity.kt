package com.amartindalonsoc.groover.activities

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.responses.Place
import com.amartindalonsoc.groover.ui.main.MapFragment
import com.amartindalonsoc.groover.ui.main.ProfileFragment
import com.amartindalonsoc.groover.ui.main.RecognizerFragment
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var placesList: List<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPlaces()


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

    fun getPlaces() {
        val currentLocation = getLocation()
        SharedPreferencesManager.saveCameraLocation(currentLocation, this)
        SharedPreferencesManager.saveMapZoom(13.25f, this)

        val request = Api.azureApiRequest()
        val call = request.getPlaces(currentLocation.latitude,currentLocation.longitude,4882.0,1,25)
        call.enqueue(object : Callback<List<Place>> {

            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                Log.i("getPlaces",response.message())
                Log.i("getPlaces",response.body().toString())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        placesList = response.body()!!
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

}