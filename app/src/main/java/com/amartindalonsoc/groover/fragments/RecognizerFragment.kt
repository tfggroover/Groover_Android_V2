package com.amartindalonsoc.groover.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import com.acrcloud.rec.ACRCloudClient
import com.acrcloud.rec.ACRCloudConfig
import com.acrcloud.rec.ACRCloudResult
import com.acrcloud.rec.IACRCloudListener
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.models.Place
import com.amartindalonsoc.groover.models.RecognizedSong
import com.amartindalonsoc.groover.models.RecognizedSpotifyArtist
import com.amartindalonsoc.groover.models.SpotifyAlbumResponse
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.round

class RecognizerFragment: Fragment(), IACRCloudListener {
    lateinit var mConfig : ACRCloudConfig
    lateinit var mClient: ACRCloudClient
    private var mProcessing = false
    private var initState = false
    private var startTime :Long = 0
    lateinit var loadingView: ProgressBar
    lateinit var loadingAndImageParams: LinearLayout.LayoutParams
    lateinit var songRecognizedImage: ImageView
    lateinit var songRecognizedName: TextView
    lateinit var songRecognizedArtist: TextView
    lateinit var recognizedFragmentContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recognizedFragmentContext = activity!!.applicationContext

        verifyPermissions()

        /// Music recognizer
        loadingView = ProgressBar(activity!!.applicationContext)
        loadingAndImageParams = LinearLayout.LayoutParams(dpToPx(200), dpToPx(200))
        songRecognizedImage = ImageView(activity!!.applicationContext)
        songRecognizedName = TextView(activity!!.applicationContext)
        songRecognizedArtist = TextView(activity!!.applicationContext)

        searchSong.setOnClickListener { start() }

        mConfig = ACRCloudConfig()

        mConfig.acrcloudListener = this
        mConfig.context = activity!!.applicationContext
// TODO Pasar a Config
        mConfig.host = "identify-eu-west-1.acrcloud.com"
        mConfig.accessKey = "1e783c710416412e75bfe54382aee5c5"
        mConfig.accessSecret = "fiZ8wJz4lcbtFNGTIGZK3lujUZ59w7U25xggL2jr"

//        mConfig.recorderConfig.rate = 8000
//        mConfig.recorderConfig.channels = 1

        mConfig.recorderConfig.isVolumeCallback = true

        this.mClient = ACRCloudClient()
        this.initState = this.mClient.initWithConfig(this.mConfig)

    }


    fun start(){
        if (!this.initState){
            Toast.makeText(activity!!.applicationContext, "init error", Toast.LENGTH_SHORT).show()
            return
        }

        if (!mProcessing){
            mProcessing = true
            loadingView.isIndeterminate = true
            removeViews()
            containter_view.addView(loadingView)
            searchSong.text = "Cancel search"
            searchSong.setOnClickListener { cancel() }

            if (this.mClient == null || !this.mClient.startRecognize()){
                mProcessing = false
            }
            startTime = System.currentTimeMillis()
        }
    }

    fun cancel(){
        if (mProcessing && this.mClient != null){
            this.mClient.cancel()
        }
        this.reset()
        loadingAndImageParams.gravity = Gravity.CENTER
        songRecognizedImage.setImageResource(R.drawable.not_recognized)
        containter_view.removeAllViews()
        containter_view.addView(songRecognizedImage)
        searchSong.text = "Try again!"
        searchSong.setOnClickListener { start() }
    }

    fun reset(){
        mProcessing = false
    }

    override fun onResult(results: ACRCloudResult) {
        this.reset()

        val result = results.result

        try {
            println(result)
            val userToken = SharedPreferencesManager.getString(Constants.spotify_user_token, recognizedFragmentContext)!!
            val resultAsJson = Gson().fromJson(result, RecognizedSong::class.java)
            val resultCode = resultAsJson.status.code

            if (resultCode == 0 && resultAsJson.metadata.music.first().externalMetadata != null && resultAsJson.metadata.music.first().externalMetadata?.spotify != null) {

                val recognizedSpotifyAlbumId = resultAsJson.metadata.music.first().externalMetadata!!.spotify!!.album.id
                val recognizedSongName = resultAsJson.metadata.music.first().title
                val recognizedSongSpotifyId = resultAsJson.metadata.music.first().externalMetadata!!.spotify!!.track.id
                val recognizedSongSpotifyName = resultAsJson.metadata.music.first().externalMetadata!!.spotify!!.track.name
                val recognizedSongArtists = resultAsJson.metadata.music.first().artists
                val recognizedSongSpotifyArtists = resultAsJson.metadata.music.first().externalMetadata!!.spotify!!.artists

//                val recognizedSongArtistList = resultAsJson.metadata.music.first().artists
                var recognizedSongArtist = recognizedSongArtists.first().name
                if (recognizedSongArtists.size > 1) {
                    for (i in 1 until recognizedSongArtists.size) {
                        recognizedSongArtist = recognizedSongArtist.plus(", " + recognizedSongArtists[i].name)
                    }
                }

                // Llamada a spotify para coger la imagen del album
                val requestForAlbumImage = Api.spotifyApiRequest()
                val callForAlbumImage = requestForAlbumImage.getAlbumWithId(recognizedSpotifyAlbumId, ("Bearer " + userToken))
                println(("Bearer " + userToken))
                callForAlbumImage.enqueue(object : Callback<SpotifyAlbumResponse> {

                    override fun onResponse(call: Call<SpotifyAlbumResponse>, response: Response<SpotifyAlbumResponse>) {
                        println("there's response" + response.raw())
                        println("album id " + recognizedSpotifyAlbumId)
                        if (response.isSuccessful) {
                            println("success")
                            if (response.body() != null) {
                                val imageUrl = response.body()!!.images.first().url
                                displayRecognizedSong(recognizedSongName, recognizedSongArtist, imageUrl)
                                //TODO Llamada a nuestro back para pasarle la id de la cancion reconocida
                                sendRecognizedSong(recognizedSongSpotifyId, recognizedSongSpotifyName, recognizedSongSpotifyArtists)
                            }
                        }
                    }

                    override fun onFailure(call: Call<SpotifyAlbumResponse>, t: Throwable) {
                        Log.i("CallbackFailure", t.message)
                    }
                })

            } else {
                Log.i("NOT_IN_SPOTIFY","Not available on Spotify")
                cancel()
            }
        }catch (e: JSONException){
            e.printStackTrace()
        }
        startTime = System.currentTimeMillis()
    }

    fun removeViews(){
        homeContentLinearLayout.removeAllViews()
        containter_view.removeAllViews()
        songRecognizedImage.setImageDrawable(null)
        songRecognizedName.text = ""
        songRecognizedArtist.text = ""
    }

    override fun onVolumeChanged(p0: Double) {
//        var time = (System.currentTimeMillis() - startTime)/1000
//        volume.text = "volume: " + p0 + "\n\nTime: " + time + " s"
    }


    override fun onDestroy() {
        super.onDestroy()
        if (this.mClient != null){
            this.mClient.release()
            this.initState = false
        }
    }

    fun displayRecognizedSong(recognizedSongName: String, recognizedSongArtist: String, recognizedSongImage: String) {
        songRecognizedName.text = recognizedSongName
        songRecognizedArtist.text = recognizedSongArtist
        Picasso.get().load(recognizedSongImage).into(songRecognizedImage)
        val textParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        songRecognizedName.gravity = Gravity.CENTER
        songRecognizedArtist.gravity = Gravity.CENTER
        songRecognizedName.layoutParams = textParams
        songRecognizedArtist.layoutParams = textParams
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(songRecognizedName, 8, 16, 1, TypedValue.COMPLEX_UNIT_SP)
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(songRecognizedArtist, 8, 16, 1, TypedValue.COMPLEX_UNIT_SP)
        loadingAndImageParams.gravity = Gravity.CENTER
        containter_view.removeAllViews()
        containter_view.addView(songRecognizedImage)
        homeContentLinearLayout.addView(songRecognizedName)
        homeContentLinearLayout.addView(songRecognizedArtist)
        searchSong.text = "Search"
        searchSong.setOnClickListener { start() }
    }


    data class RecognizedSongForBack (val id: String, val name: String, val artists: List<RecognizedSpotifyArtist>)
    fun sendRecognizedSong(id: String?, name: String?, artists: List<RecognizedSpotifyArtist>?) {
        if (id != null && name != null && artists != null) {
            val recognizedSongToSend = RecognizedSongForBack(id, name, artists)
            val currentLocation = getLocation()
            val request = Api.azureApiRequest()
            val call = request.getPlaces(currentLocation.latitude,currentLocation.longitude,50.0,1,25)
            call.enqueue(object : Callback<List<Place>> {

                override fun onResponse(call: Call<List<Place>>, response: Response<List<Place>>) {
                    if (response.isSuccessful) {
                        if (response.body() != null && response.body()!!.isNotEmpty()) {
                            val firebaseBearer = SharedPreferencesManager.getFirebaseBearer(recognizedFragmentContext)
                            Log.i("BEARER", firebaseBearer)
                            request.addRecognizedSong(response.body()!!.first().id, ("Bearer " + firebaseBearer),recognizedSongToSend).enqueue(object: Callback<Any> {
                                override fun onFailure(call: Call<Any>, t: Throwable) {
                                    Log.i("SongNotAdded", t.message)
                                }

                                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                    Log.i("SongAdded", "Successfully added song to place")
                                }
                            })
                        }
                    }
                }

                override fun onFailure(call: Call<List<Place>>, t: Throwable) {
                    Log.i("PlacesFailure", t.message)
                }

            })
        }
    }


    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS = arrayOf<String>(
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.INTERNET,
        Manifest.permission.RECORD_AUDIO
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
        val locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

    /////// Aux functions, move later
    fun dpToPx(dp: Int): Int {
        val density = this.resources.displayMetrics.density
        return round(dp * density).toInt()
    }
}