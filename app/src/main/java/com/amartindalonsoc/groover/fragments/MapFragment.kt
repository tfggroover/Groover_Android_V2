package com.amartindalonsoc.groover.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.activities.MainActivity
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.fragments.PlaylistTypeInPlacePagerAdapter
import com.amartindalonsoc.groover.models.Place
import com.amartindalonsoc.groover.models.Song
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.MainPlaylistAdapter
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.playlist_fragment.*
import kotlinx.android.synthetic.main.recognized_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class MapFragment: Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var sheetBehavior : BottomSheetBehavior<LinearLayout>
    private lateinit var mainPlaylistLinearLayoutManager: LinearLayoutManager
    private lateinit var mainPlaylistAdapter: MainPlaylistAdapter
    private lateinit var recognizedSongsLinearLayoutManager: LinearLayoutManager
    private lateinit var recognizedSongsAdapter: MainPlaylistAdapter

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

        val fragmentAdapter = PlaylistTypeInPlacePagerAdapter(childFragmentManager)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)

         val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
         mapFragment?.getMapAsync(this)

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


        mainPlaylistLinearLayoutManager = LinearLayoutManager(mapFragmentContext)
        main_playlist_recycler_view.layoutManager = mainPlaylistLinearLayoutManager
        recognizedSongsLinearLayoutManager = LinearLayoutManager(mapFragmentContext)
        recognized_songs_recycler_view.layoutManager = recognizedSongsLinearLayoutManager
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

            val emptyList = listOf<Song>()
            Log.i("CHILD_COUNT", main_playlist_recycler_view.childCount.toString())

//            if (main_playlist_recycler_view.childCount > 0) {
//                mainPlaylistAdapter = MainPlaylistAdapter(emptyList)
//                main_playlist_recycler_view.adapter = mainPlaylistAdapter
//            }


            val place = marker!!.tag as Place
            if (place.mainPlaylist != null) {
                bottom_sheet.playlistName.text = place.mainPlaylist.name
                mainPlaylistAdapter = MainPlaylistAdapter(place.mainPlaylist.songs)
                main_playlist_recycler_view.adapter = mainPlaylistAdapter
                if(place.mainPlaylist.imageUrl != ""){
                    Picasso.get().load(place.mainPlaylist.imageUrl).into(bottom_sheet.playlistImage)
                } else {
                    bottom_sheet.playlistImage.setImageResource(R.drawable.ic_profile_dark_foreground)
                }
            } else { //TODO Revisar que con este metodo, cuando se pasa de una place con canciones a otra con canciones, no se vayan sumando las playlists
                bottom_sheet.playlistName.text = ""
                mainPlaylistAdapter = MainPlaylistAdapter(emptyList)
                main_playlist_recycler_view.adapter = mainPlaylistAdapter
                bottom_sheet.playlistImage.setImageResource(R.drawable.ic_profile_dark_foreground)
            }
            if (place.recognizedMusic != null) {
                // Comprobar que esto sea correcto
                recognizedSongsAdapter = MainPlaylistAdapter(listOf()) // TODO Cambiar el listOf() por la lista real de canciones, si no son Songs, voy a tener que crear otro adapter
                recognized_songs_recycler_view.adapter = recognizedSongsAdapter
            }

            // TODO Meterle lo mismo al recognizedAdapter, ver si se puede reutilizar el de MainPlaylist, porque en teoria las canciones contienen lo mismo exactamente
            setRecyclerViewScrollListener()


            // TODO Cambiar el string del horario de forma que pueda ser traducido a diferentes idiomas

//            bottom_sheet.expand_bar_open.setOnClickListener {
//                expandSetOnClickListener(place, bottom_sheet.bar_open, bottom_sheet.expand_bar_open)
//            }

            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottom_sheet.bar_name.text = marker.title
            bottom_sheet.bar_address.text = place.address
            bottom_sheet.bar_phone.text = place.phone

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


    fun getPlaces() {
        val request = Api.azureApiRequest()
        val call = request.getPlaces(centerCoords.latitude,centerCoords.longitude,distance,1,25)
        call.enqueue(object : Callback<List<Place>> {

            override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                Log.i("getPlaces",response.message())
                Log.i("getPlaces",response.body().toString())
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        (activity as MainActivity).placesList = response.body()!!
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
        for (place in (activity as MainActivity).placesList) {
            val marker = mMap.addMarker(
                MarkerOptions().position(LatLng(place.location.latitude,place.location.longitude)).title(place.displayName)
            )
            marker.tag = place
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

    //TODO Revisar si esto se utiliza bien y quitar codigo sobrante
    private val lastVisibleItemPosition: Int
        get() = mainPlaylistLinearLayoutManager.findLastVisibleItemPosition()

    private fun setRecyclerViewScrollListener() {
        main_playlist_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
            }
        })
    }

}

