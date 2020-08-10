package com.amartindalonsoc.groover.ui.main

import android.Manifest
import android.app.Service
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
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import com.acrcloud.rec.*
import com.acrcloud.rec.ACRCloudClient
import com.acrcloud.rec.ACRCloudConfig
import com.acrcloud.rec.ACRCloudResult
import com.acrcloud.rec.IACRCloudListener
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.responses.RecognizedSong
import com.amartindalonsoc.groover.responses.SpotifyAlbumResponse
import com.amartindalonsoc.groover.responses.SpotifyCallback
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.amartindalonsoc.groover.utils.Utils
import com.google.gson.Gson
import com.squareup.picasso.Picasso
//import com.acrcloud.rec.ACRCloudClient
//import com.acrcloud.rec.ACRCloudConfig
//import com.acrcloud.rec.ACRCloudResult
//import com.acrcloud.rec.IACRCloudListener
//import com.acrcloud.rec.utils.ACRCloudLogger
//import com.amartindalonsoc.groover.R
//import com.amartindalonsoc.groover.ui.login.StoredUser
//import com.android.volley.Request
//import com.android.volley.Response
//import com.android.volley.toolbox.JsonObjectRequest
//import com.google.firebase.firestore.FirebaseFirestore
//import com.spotify.protocol.types.Album
//import com.spotify.protocol.types.Track
//import com.squareup.moshi.Moshi
//import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.abs
import kotlin.math.round

class RecognizerFragment: Fragment(), IACRCloudListener {
    //lateinit var globalVars :GlobalVars
    //lateinit var storedUser: StoredUser
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
//    val db = FirebaseFirestore.getInstance()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //globalVars  = activity!!.applicationContext as GlobalVars
    }

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
//        globalVars.currentFragment = "home"

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
//            volume.text = ""
//            songRecognized.text = ""
            loadingAndImageParams.gravity = Gravity.CENTER
            loadingView.isIndeterminate = true
            loadingView.layoutParams = loadingAndImageParams
            removeViews()
            homeContentLinearLayout.addView(loadingView)
            searchSong.text = "Cancel search"
            searchSong.setOnClickListener { cancel() }

            if (this.mClient == null || !this.mClient.startRecognize()){
                mProcessing = false
//                songRecognized.text = "start error!"
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
        homeContentLinearLayout.removeView(loadingView)
        homeContentLinearLayout.addView(songRecognizedImage)
        searchSong.text = "Try again!"
        searchSong.setOnClickListener { start() }
    }

    fun reset(){
//        songRecognized.text = ""
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

                val recognizedSongArtistList = resultAsJson.metadata.music.first().artists
                var recognizedSongArtist = ""
                if (recognizedSongArtistList.size > 1) {
                    for (artist in recognizedSongArtistList) {
                        recognizedSongArtist = recognizedSongArtist.plus(", " + artist.name)
                    }
                } else {
                    recognizedSongArtist = recognizedSongArtistList.first().name
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
                            }
                        }
                    }

                    override fun onFailure(call: Call<SpotifyAlbumResponse>, t: Throwable) {
                        Log.i("CallbackFailure", t.message)
                    }
                })

                //TODO Llamada a nuestro back para pasarle la id de la cancion reconocida
            } else {
                cancel()
            }
        }catch (e: JSONException){
            e.printStackTrace()
        }
        startTime = System.currentTimeMillis()
    }

    fun removeViews(){
        homeContentLinearLayout.removeAllViews()
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

            //this.mClient = null
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
        homeContentLinearLayout.removeView(loadingView)
        homeContentLinearLayout.addView(songRecognizedImage)
        homeContentLinearLayout.addView(songRecognizedName)
        homeContentLinearLayout.addView(songRecognizedArtist)
        searchSong.text = "Search"
        searchSong.setOnClickListener { start() }
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

    /////// Aux functions, move later
    fun dpToPx(dp: Int): Int {
        val density = this.resources.displayMetrics.density
        return round(dp * density).toInt()
    }
}