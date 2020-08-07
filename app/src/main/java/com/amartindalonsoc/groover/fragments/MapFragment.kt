package com.amartindalonsoc.groover.ui.main

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import com.amartindalonsoc.groover.R
//import com.amartindalonsoc.groover.ui.login.StoredUser
//import com.android.volley.Request
//import com.android.volley.Response
//import com.android.volley.toolbox.JsonObjectRequest
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MapStyleOptions
//import com.google.android.gms.maps.model.Marker
//import com.google.android.gms.maps.model.MarkerOptions
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//import com.google.firebase.firestore.DocumentChange
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Source
//import com.spotify.protocol.types.Track
//import com.squareup.moshi.Moshi
//import com.squareup.picasso.Picasso
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.bottom_sheet.*
//import kotlinx.android.synthetic.main.bottom_sheet.view.*
//import kotlinx.android.synthetic.main.recognized_fragment.*
//import kotlinx.android.synthetic.main.playlist_fragment.*

import kotlinx.android.synthetic.main.activity_map.map
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import java.time.DayOfWeek
import java.util.*
import kotlin.math.round

// TODO Guardar datos una vez cargado el mapa por primera vez, evitar que cada vez que vas a la pestaña del mapa, haga solicitudes a la BD si no ha habido cambios en gustos o algo similar

class MapFragment: Fragment()/*, OnMapReadyCallback*/ {

//    private lateinit var mMap: GoogleMap
//    private lateinit var sheetBehavior : BottomSheetBehavior<LinearLayout>
//    var expanded = false
//    /** Keeps track of the selected marker. It will be set to null if no marker is selected. */
//    private var selectedMarker: Marker? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(R.layout.fragment_map, null)
//    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        GlobalVars.currentFragment = "map"
//
//        val fragmentAdapter = MyPagerAdapter(childFragmentManager)
//        viewpager_main.adapter = fragmentAdapter
//        tabs_main.setupWithViewPager(viewpager_main)
//
//         val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
//         mapFragment?.getMapAsync(this)
//
////        toolbarToChange.title = "Groover"
//
//        sheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottom_sheet)
//        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//    }
//
//
//
//    /**
//     * If user tapped on the the marker which was already showing info window,
//     * the showing info window will be closed. Otherwise will show a different window.
//     */
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//        mMap.uiSettings.isMapToolbarEnabled = false
//        mMap.isMyLocationEnabled = true
//
//        for (place in GlobalVars.places) {
//            mMap.addMarker(
//                MarkerOptions().position(LatLng(place.value.location.latitude,place.value.location.longitude)).title(place.value.displayname)
//            )
//        }
//
//
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.map_style))
//
//
//        var currentLocation = (activity as MainActivity).getLocation()
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude,currentLocation.longitude),13.25f))
//
//        mMap.setOnMarkerClickListener(markerClickListener)
//        mMap.setOnMapClickListener {
//            selectedMarker = null
//            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//        }
//    }
//
//    private val markerClickListener = object: GoogleMap.OnMarkerClickListener {
//        override fun onMarkerClick(marker: Marker?): Boolean {
//            if (marker == selectedMarker) {
//                // Return true to indicate we have consumed the event and that we do not
//                // want the the default behavior to occur (which is for the camera to move
//                // such that the marker is centered and for the marker's info window to open,
//                // if it has one).
//
//                //selectedMarker = null
//                //sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//                // Al final se ha decidido no hacer nada cuando se vuelve a pinchar en el mismo marcador
//                marker!!.showInfoWindow()
//                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                return true
//            }
//
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
//
//            // TODO Cambiar el string del horario de forma que pueda ser traducido a diferentes idiomas
//
//            bottom_sheet.expand_bar_open.setOnClickListener {
//                expandSetOnClickListener(place, bottom_sheet.bar_open, bottom_sheet.expand_bar_open)
//            }
//
//
//            if(place.playlist["id"] != "") {
//                val debug1 = place.playlistData
//                bottom_sheet.playlistName.text = place.playlistData!!.name
//                bottom_sheet.playlistName.isSelected = true
//                bottom_sheet.playlistImage.setImageBitmap(place.playlistData?.image)
//
//                for (song in place.playlistData!!.tracks) {
//                    //TODO Meter aqui el generador de LinearLayout de la app de DnD
//                    val songView = LinearLayout(context)
//                    val songViewParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        dpToPx(40)
//                    )
//                    songViewParams.setMargins(0, 0, 0, dpToPx(1))
//                    songView.orientation = LinearLayout.HORIZONTAL
//                    songView.layoutParams = songViewParams
//
//                    val songImageView = ImageView(context)
//                    val songImageViewParams =
//                        LinearLayout.LayoutParams(dpToPx(40), dpToPx(40))
//                    songImageView.layoutParams = songImageViewParams
//
//                    if (StoredUser.spotifyUser.product == "premium" && GlobalVars.spotifyInstalled){
//                        songImageView.setBackgroundResource(R.drawable.ic_play_foreground)
//                        songImageView.setOnClickListener {
//                            GlobalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
//                            GlobalVars.mSpotifyAppRemote.playerApi.play(song.uri)
//                        }
//                    }
//
//                    val songTextView = TextView(context)
//                    val songTextViewParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.MATCH_PARENT
//                    )
//                    songTextViewParams.setMargins(dpToPx(5), 0, 0, 0)
//                    songTextView.layoutParams = songTextViewParams
//                    songTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
//                    songTextView.marqueeRepeatLimit = -1
//                    songTextView.setSingleLine(true)
//                    songTextView.isSelected = true
//                    songTextView.gravity = Gravity.CENTER_VERTICAL
//                    songTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//                    songTextView.text =
//                        song.name + " [" + song.artists[0].name + "]"
//
//                    songView.addView(songImageView)
//                    songView.addView(songTextView)
//                    playlistContent.addView(songView)
//
//                }
//            }else{
//                bottom_sheet.playlistName.text = "No official playlist"
//                bottom_sheet.playlistImage.setImageDrawable(null)
//            }
//            var recognizedSongs = ""
//            var added = 0
//            //TODO Modificar el tratamiento de recognized
//
//            /*if(place.recognized != ""){
//                db.collection("music_recognition").document(place.recognized).get().addOnSuccessListener { document ->
//                    val musicLocation = document.toObject(MusicLocation::class.java)
//                    if (musicLocation?.songs != null){
//                        for (song in musicLocation.songs){
//                            recognizedSongs = recognizedSongs + song.key + "%2C"
//                            added += 1
//                            if (added >= 50){
//                                break
//                            }
//                        }
//                        if (musicLocation.songs.isNotEmpty()){
//                            recognizedSongs = recognizedSongs.substring(0, recognizedSongs.length - 3)
//                        }
//
//                        *//*if (recognizedSongs != ""){
//                            val recognizedRequest = object:  JsonObjectRequest(
//                                Request.Method.GET, "https://api.spotify.com/v1/tracks?ids=" + recognizedSongs, null,
//                                Response.Listener<JSONObject>{ jsonResponse ->
//                                    val songsArray = jsonResponse.getJSONArray("tracks")
//                                    val moshi = Moshi.Builder().build()
//                                    val jsonAdapter = moshi.adapter(Track::class.java)
//                                    for(i in 0 until (songsArray.length())){
//                                        val track = jsonAdapter.fromJson(songsArray[i].toString())
//                                        if (track != null){
//                                            val songView = LinearLayout(context)
//                                            val songViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40))
//                                            songViewParams.setMargins(0,0,0,dpToPx(1))
//                                            songView.orientation = LinearLayout.HORIZONTAL
//                                            songView.layoutParams = songViewParams
//
//                                            val songImageView = ImageView(context)
//                                            val songImageViewParams = LinearLayout.LayoutParams(dpToPx(40),dpToPx(40))
//                                            songImageView.layoutParams = songImageViewParams
//
//                                            if (storedUser.spotifyUser.product == "premium" && globalVars.spotifyInstalled){
//                                                songImageView.setBackgroundResource(R.drawable.ic_play_foreground)
//                                                songImageView.setOnClickListener {
//                                                    globalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
//                                                    globalVars.mSpotifyAppRemote.playerApi.play(track.uri)
//                                                }
//                                            }
//
//                                            val songTextView = TextView(context)
//                                            val songTextViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
//                                            songTextViewParams.setMargins(dpToPx(5),0,0,0)
//                                            songTextView.layoutParams = songTextViewParams
//                                            songTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
//                                            songTextView.marqueeRepeatLimit = -1
//                                            songTextView.setSingleLine(true)
//                                            songTextView.isSelected = true
//                                            songTextView.gravity = Gravity.CENTER_VERTICAL
//                                            songTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16f)
//                                            songTextView.text = track.name + " [" + track.artists[0].name + "]"
////                                                    TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(songTextView, 8, 16, 1, TypedValue.COMPLEX_UNIT_SP)
//
//                                            songView.addView(songImageView)
//                                            songView.addView(songTextView)
//                                            recognizedContent.addView(songView)
//                                        }
//                                    }
//
//                                },
//                                Response.ErrorListener {error ->}){
//
//                                override fun getHeaders(): MutableMap<String, String> {
//                                    val requestHeader: MutableMap<String,String> = hashMapOf()
//                                    requestHeader.put("Accept","application/json")
//                                    requestHeader.put("Content-Type","application/json")
//                                    requestHeader.put("Authorization","Bearer " + storedUser.access)
//
//                                    return requestHeader
//                                }
//                            }
//                            MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(recognizedRequest)
//                        }*//*
//                    }
//
//                }
//            }*/
//
//            if(place.playlist["id"] != "") {
//
//                if (StoredUser.spotifyUser.product == "premium" && GlobalVars.spotifyInstalled){
//                    val imagePlay = bottom_sheet.playlistPlay
//                    imagePlay.setBackgroundResource(R.drawable.ic_play_foreground)
//                    imagePlay.setOnClickListener {
//                        GlobalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
//                        GlobalVars.mSpotifyAppRemote.playerApi.play("spotify:playlist:" + place.playlist)
//                    }
//                }
//            }
//
//            /*
//            lateinit var playlistRequest :JsonObjectRequest
//            if(place.playlist["id"] != "") {
//                playlistRequest = object : JsonObjectRequest(
//                    Request.Method.GET,
//                    "https://api.spotify.com/v1/playlists/" + place.playlist["id"] + "?fields=name%2Cimages%2Ctracks.items(track(name%2Chref%2Curi%2Cartists(name)%2Calbum(name%2Cimages)))",
//                    null,
//                    Response.Listener<JSONObject> { jsonResponse ->
//                        val itemsArray =
//                            jsonResponse.getJSONObject("tracks").getJSONArray("items")
//                        val moshi = Moshi.Builder().build()
//                        val jsonAdapter = moshi.adapter(Track::class.java)
//
//                        var name = jsonResponse.get("name").toString()
//                        bottom_sheet.playlistName.text = name
//                        bottom_sheet.playlistName.isSelected = true
//                        Picasso.get().load(
//                            JSONObject(jsonResponse.getJSONArray("images")[0].toString()).get(
//                                "url"
//                            ).toString()
//                        ).into(bottom_sheet.playlistImage)
//
//                        val plt = Playlist()
//                        for (i in 0 until (itemsArray.length())) {
//                            val track = jsonAdapter.fromJson(
//                                JSONObject(itemsArray[i].toString()).getJSONObject("track").toString()
//                            )
//                            if (track != null) {
//                                plt.tracks.add(track)
//                            }
//                        }
//
//                        for (song in plt.tracks) {
//                            //TODO Meter aqui el generador de LinearLayout de la app de DnD
//                            val songView = LinearLayout(context)
//                            val songViewParams = LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.MATCH_PARENT,
//                                dpToPx(40)
//                            )
//                            songViewParams.setMargins(0, 0, 0, dpToPx(1))
//                            songView.orientation = LinearLayout.HORIZONTAL
//                            songView.layoutParams = songViewParams
//
//                            val songImageView = ImageView(context)
//                            val songImageViewParams =
//                                LinearLayout.LayoutParams(dpToPx(40), dpToPx(40))
//                            songImageView.layoutParams = songImageViewParams
//
//                            if (StoredUser.spotifyUser.product == "premium" && GlobalVars.spotifyInstalled){
//                                songImageView.setBackgroundResource(R.drawable.ic_play_foreground)
//                                songImageView.setOnClickListener {
//                                    GlobalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
//                                    GlobalVars.mSpotifyAppRemote.playerApi.play(song.uri)
//                                }
//                            }
//
//                            val songTextView = TextView(context)
//                            val songTextViewParams = LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.MATCH_PARENT,
//                                LinearLayout.LayoutParams.MATCH_PARENT
//                            )
//                            songTextViewParams.setMargins(dpToPx(5), 0, 0, 0)
//                            songTextView.layoutParams = songTextViewParams
//                            songTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
//                            songTextView.marqueeRepeatLimit = -1
//                            songTextView.setSingleLine(true)
//                            songTextView.isSelected = true
//                            songTextView.gravity = Gravity.CENTER_VERTICAL
//                            songTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//                            songTextView.text =
//                                song.name + " [" + song.artists[0].name + "]"
//
//                            songView.addView(songImageView)
//                            songView.addView(songTextView)
//                            playlistContent.addView(songView)
//
//                        }
//
//                    },
//                    Response.ErrorListener { error -> }) {
//
//                    override fun getHeaders(): MutableMap<String, String> {
//                        val requestHeader: MutableMap<String, String> = hashMapOf()
//                        requestHeader.put("Accept", "application/json")
//                        requestHeader.put("Content-Type", "application/json")
//                        requestHeader.put("Authorization", "Bearer " + StoredUser.access)
//
//                        return requestHeader
//                    }
//                }
//            }else{
//                bottom_sheet.playlistName.text = "No official playlist"
//                bottom_sheet.playlistImage.setImageDrawable(null)
//            }
//            var recognizedSongs = ""
//            var added = 0
//            //TODO Modificar el tratamiento de recognized
//
//            *//*if(place.recognized != ""){
//                db.collection("music_recognition").document(place.recognized).get().addOnSuccessListener { document ->
//                    val musicLocation = document.toObject(MusicLocation::class.java)
//                    if (musicLocation?.songs != null){
//                        for (song in musicLocation.songs){
//                            recognizedSongs = recognizedSongs + song.key + "%2C"
//                            added += 1
//                            if (added >= 50){
//                                break
//                            }
//                        }
//                        if (musicLocation.songs.isNotEmpty()){
//                            recognizedSongs = recognizedSongs.substring(0, recognizedSongs.length - 3)
//                        }
//
//                        if (recognizedSongs != ""){
//                            val recognizedRequest = object:  JsonObjectRequest(
//                                Request.Method.GET, "https://api.spotify.com/v1/tracks?ids=" + recognizedSongs, null,
//                                Response.Listener<JSONObject>{ jsonResponse ->
//                                    val songsArray = jsonResponse.getJSONArray("tracks")
//                                    val moshi = Moshi.Builder().build()
//                                    val jsonAdapter = moshi.adapter(Track::class.java)
//                                    for(i in 0 until (songsArray.length())){
//                                        val track = jsonAdapter.fromJson(songsArray[i].toString())
//                                        if (track != null){
//                                            val songView = LinearLayout(context)
//                                            val songViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40))
//                                            songViewParams.setMargins(0,0,0,dpToPx(1))
//                                            songView.orientation = LinearLayout.HORIZONTAL
//                                            songView.layoutParams = songViewParams
//
//                                            val songImageView = ImageView(context)
//                                            val songImageViewParams = LinearLayout.LayoutParams(dpToPx(40),dpToPx(40))
//                                            songImageView.layoutParams = songImageViewParams
//
//                                            if (storedUser.spotifyUser.product == "premium" && globalVars.spotifyInstalled){
//                                                songImageView.setBackgroundResource(R.drawable.ic_play_foreground)
//                                                songImageView.setOnClickListener {
//                                                    globalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
//                                                    globalVars.mSpotifyAppRemote.playerApi.play(track.uri)
//                                                }
//                                            }
//
//                                            val songTextView = TextView(context)
//                                            val songTextViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
//                                            songTextViewParams.setMargins(dpToPx(5),0,0,0)
//                                            songTextView.layoutParams = songTextViewParams
//                                            songTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
//                                            songTextView.marqueeRepeatLimit = -1
//                                            songTextView.setSingleLine(true)
//                                            songTextView.isSelected = true
//                                            songTextView.gravity = Gravity.CENTER_VERTICAL
//                                            songTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16f)
//                                            songTextView.text = track.name + " [" + track.artists[0].name + "]"
////                                                    TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(songTextView, 8, 16, 1, TypedValue.COMPLEX_UNIT_SP)
//
//                                            songView.addView(songImageView)
//                                            songView.addView(songTextView)
//                                            recognizedContent.addView(songView)
//                                        }
//                                    }
//
//                                },
//                                Response.ErrorListener {error ->}){
//
//                                override fun getHeaders(): MutableMap<String, String> {
//                                    val requestHeader: MutableMap<String,String> = hashMapOf()
//                                    requestHeader.put("Accept","application/json")
//                                    requestHeader.put("Content-Type","application/json")
//                                    requestHeader.put("Authorization","Bearer " + storedUser.access)
//
//                                    return requestHeader
//                                }
//                            }
//                            MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(recognizedRequest)
//                        }
//                    }
//
//                }
//            }*//*
//
//            if(place.playlist["id"] != "") {
//                MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(playlistRequest)
//
//                if (StoredUser.spotifyUser.product == "premium" && GlobalVars.spotifyInstalled){
//                    val imagePlay = bottom_sheet.playlistPlay
//                    imagePlay.setBackgroundResource(R.drawable.ic_play_foreground)
//                    imagePlay.setOnClickListener {
//                        GlobalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
//                        GlobalVars.mSpotifyAppRemote.playerApi.play("spotify:playlist:" + place.playlist)
//                    }
//                }
//
//
//            }*/
//
//
//
//            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            val barName = bottom_sheet.bar_name
//            barName.text = marker?.title
//            selectedMarker = marker
//            // Return false to indicate that we have not consumed the event and that
//            // we wish for the default behavior to occur.
//            return false
//        }
//    }
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
}