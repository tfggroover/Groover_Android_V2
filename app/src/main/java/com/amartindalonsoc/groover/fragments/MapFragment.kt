package com.amartindalonsoc.groover.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.fragments.PlaylistTypeInPlacePagerAdapter
import com.amartindalonsoc.groover.responses.Place
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.amartindalonsoc.groover.utils.Utils
import com.google.android.gms.maps.CameraUpdateFactory
//import com.amartindalonsoc.groover.ui.login.StoredUser
//import com.android.volley.Request
//import com.android.volley.Response
//import com.android.volley.toolbox.JsonObjectRequest
//import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MapStyleOptions
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.spotify.protocol.types.Track
//import com.squareup.moshi.Moshi
//import com.squareup.picasso.Picasso
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.bottom_sheet.*
//import kotlinx.android.synthetic.main.bottom_sheet.view.*
//import kotlinx.android.synthetic.main.recognized_fragment.*
//import kotlinx.android.synthetic.main.playlist_fragment.*


class MapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var sheetBehavior : BottomSheetBehavior<LinearLayout>
    lateinit var mapFragmentContext: Context
    lateinit var centerCoords: LatLng
    var distance = 4900.0
    var mapMoved = false
    var expanded = false
//    /** Keeps track of the selected marker. It will be set to null if no marker is selected. */
    private var selectedMarker: Marker? = null
//
    override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, null)
    }
//
//
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapFragmentContext = activity!!.applicationContext
//        GlobalVars.currentFragment = "map"

        val fragmentAdapter = PlaylistTypeInPlacePagerAdapter(childFragmentManager)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)

         val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
         mapFragment?.getMapAsync(this)

//        toolbarToChange.title = "Groover"

        sheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottom_sheet)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
//
//
//
//    /**
//     * If user tapped on the the marker which was already showing info window,
//     * the showing info window will be closed. Otherwise will show a different window.
//     */
    @SuppressLint("MissingPermission") // Permission check inside verifyPermissions()
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMapToolbarEnabled = false
        verifyPermissions()
        mMap.isMyLocationEnabled = true

        showPlaces()

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.map_style))


        val currentLocation = SharedPreferencesManager.getCameraLocation(mapFragmentContext)
        val zoom = SharedPreferencesManager.getMapZoom(mapFragmentContext)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude,currentLocation.longitude),zoom))

        mMap.setOnMarkerClickListener(markerClickListener)
        mMap.setOnMapClickListener {
            selectedMarker = null
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        mMap.setOnCameraIdleListener(camerChangeListener)
        mMap.setOnCameraMoveListener {
            Log.i("mapIdle", "mapMoved")
            mapMoved = true
        }

        searchAreaButton.setOnClickListener {
            getPlaces()
        }
    }

    private val camerChangeListener = object: GoogleMap.OnCameraIdleListener {
        override fun onCameraIdle() {
            val northEastCoord = mMap.projection.visibleRegion.latLngBounds.northeast
            centerCoords = mMap.projection.visibleRegion.latLngBounds.center
            distance = coordsDistance(northEastCoord, centerCoords)
            SharedPreferencesManager.saveFloat(Constants.map_distance_to_corner, distance.toFloat(), mapFragmentContext)
            SharedPreferencesManager.saveMapZoom(mMap.cameraPosition.zoom, mapFragmentContext)
            SharedPreferencesManager.saveCameraLocation(centerCoords, mapFragmentContext)
            if (mapMoved) { searchAreaButton.visibility = Button.VISIBLE }
        }
    }

    private val markerClickListener = object: GoogleMap.OnMarkerClickListener {
        override fun onMarkerClick(marker: Marker?): Boolean {
            if (marker == selectedMarker) {
                // Return true to indicate we have consumed the event and that we do not
                // want the the default behavior to occur (which is for the camera to move
                // such that the marker is centered and for the marker's info window to open,
                // if it has one).

                //selectedMarker = null
                //sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                // Al final se ha decidido no hacer nada cuando se vuelve a pinchar en el mismo marcador
                marker!!.showInfoWindow()
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                return true
            }

            val placeId = marker!!.tag as String
            val address = SharedPreferencesManager.getString(placeId.plus(Constants.places_address), mapFragmentContext)
            bottom_sheet.bar_address.text = address


//            val debug2 = GlobalVars.places
//            val place: Place = GlobalVars.places.getValue(marker!!.title)
//            expanded = false
//            bottom_sheet.bar_name.text = place.displayname
//            bottom_sheet.bar_name.isSelected = true
//            bottom_sheet.rating.text = place.rating.toString()
//            bottom_sheet.ratingBar.rating = place.rating.toFloat()
//            bottom_sheet.bar_address.text = place.address
//            bottom_sheet.bar_phone.text = place.phone
//            if (playlistContent != null){
//                playlistContent.removeAllViews()
//            }
//            if (recognizedContent != null){
//                recognizedContent.removeAllViews()
//            }
//            val calendar = Calendar.getInstance()
//            val today = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).toLowerCase()
//            bottom_sheet.bar_open.text = getString(R.string.open_today, timetableDayText(place,today))

            // TODO Cambiar el string del horario de forma que pueda ser traducido a diferentes idiomas

//            bottom_sheet.expand_bar_open.setOnClickListener {
//                expandSetOnClickListener(place, bottom_sheet.bar_open, bottom_sheet.expand_bar_open)
//            }

            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            val barName = bottom_sheet.bar_name
            barName.text = marker?.title
            selectedMarker = marker
            // Return false to indicate that we have not consumed the event and that
            // we wish for the default behavior to occur.
            return false
        }
    }
//
//    fun expandSetOnClickListener(place: Place, openingHours: TextView, expandedImage: ImageView){
//        if (expanded){
//            expandedImage.setImageResource(R.drawable.ic_expand_more)
//
//            val calendar = Calendar.getInstance()
//            val today = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()).toLowerCase()
//            openingHours.text = getString(R.string.open_today, timetableDayText(place,today))
//
//            expanded = false
//        }else{
//            expandedImage.setImageResource(R.drawable.ic_expand_less)
//
//            openingHours.text = getString(R.string.open_times,
//                                            timetableDayText(place,"monday"),
//                                            timetableDayText(place,"tuesday"),
//                                            timetableDayText(place,"wednesday"),
//                                            timetableDayText(place,"thursday"),
//                                            timetableDayText(place,"friday"),
//                                            timetableDayText(place,"saturday"),
//                                            timetableDayText(place,"sunday"))
//            expanded = true
//        }
//    }
//
//    fun timetableDayText(place: Place, day: String): String {
//        var dayText = ""
//
//        if (place.timetables.containsKey(day)){
//            val dayOpeningMap = place.timetables[day]
//            val dayOpeningKeys = dayOpeningMap?.keys?.sorted()
//
//            if (dayOpeningKeys != null){
//                for (startTime in dayOpeningKeys){
//                    if (startTime == "Closed"){
//                        dayText = "Closed"
//                    }else{
//                        dayText = dayText.plus(startTime + " - " + dayOpeningMap?.get(startTime) + " and ")
//                    }
//                }
//                if (dayText.takeLast(5) == " and "){
//                    dayText = dayText.dropLast(5)
//                }
//            }else{
//                dayText = "Closed"
//            }
//        }else{
//            dayText = "Closed"
//        }
//        return dayText
//    }
//
//    /////// Aux functions, move later
//    fun dpToPx(dp: Int): Int {
//        val density = this.resources.displayMetrics.density
//        return round(dp * density).toInt()
//    }
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
            val permission = ActivityCompat.checkSelfPermission(activity!!.applicationContext, PERMISSIONS[i])
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!, PERMISSIONS,
                    REQUEST_EXTERNAL_STORAGE
                )
                break
            }
        }
    }


    // Location
    @SuppressLint("MissingPermission")
    fun getLocation(): LatLng {
        val locationManager = mapFragmentContext.getSystemService(LOCATION_SERVICE) as LocationManager
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
    fun getPlaces() { // TODO Volver a pedir las places en profile si se modifica la distancia, mirar como hacer para descargar todo lo que esté en la misma ciudad y luego mostrar simplemente en función de la distancia
        val request = Api.azureApiRequest()
        val call = request.getPlaces(centerCoords.latitude,centerCoords.longitude,distance,1,25)
        call.enqueue(object : Callback<List<Place>> {

            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                Log.i("getPlaces",response.message())
                Log.i("getPlaces",response.body().toString())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        //TODO Guardar datos y mandar a MainActivity
                        SharedPreferencesManager.savePlacesFromCallback(response.body()!!, mapFragmentContext)

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
                        mMap.clear()
                        showPlaces()
                        searchAreaButton.visibility = Button.INVISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                Log.i("CallbackFailurePlaces", t.message)
            }

        })
    }

    fun showPlaces() {
        val placesIds = SharedPreferencesManager.getPlacesIds(mapFragmentContext)
        if (placesIds != null) {
            for (place in placesIds) {
                val displayName = SharedPreferencesManager.getString(place.plus(Constants.places_display_name), mapFragmentContext)
                val latitude = SharedPreferencesManager.getFloat(place.plus(Constants.places_latitude), mapFragmentContext)
                val longitude = SharedPreferencesManager.getFloat(place.plus(Constants.places_longitude), mapFragmentContext)

                val marker = mMap.addMarker(
                    MarkerOptions().position(LatLng(latitude!!.toDouble(),longitude!!.toDouble())).title(displayName)
                )
                marker.tag = place
            }
        }
    }

    private fun coordsDistance(northEastCoords: LatLng, centerCoords: LatLng): Double {
        val r = 6371e3 // metres
        val lat1 = degreesToRadians(northEastCoords.latitude)
        val lat2 = degreesToRadians(centerCoords.latitude)
        val deltaLat = degreesToRadians(centerCoords.latitude - northEastCoords.latitude)
        val deltaLong = degreesToRadians(centerCoords.longitude - northEastCoords.longitude)

        val a = sin(deltaLat/2) * sin(deltaLat/2) +
                cos(lat1) * cos(lat2) *
                sin(deltaLong/2) * sin(deltaLong/2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    private fun degreesToRadians(deg: Double): Double {
        return deg * (Math.PI / 180)
    }
}