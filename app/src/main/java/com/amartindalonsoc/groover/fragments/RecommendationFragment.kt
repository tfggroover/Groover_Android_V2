package com.amartindalonsoc.groover.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.models.ItemForRecommendation
import com.amartindalonsoc.groover.models.SpotifyTopTracksResponse
import com.amartindalonsoc.groover.models.SpotifyUserPlaylistsResponse
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.MainPlaylistAdapter
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.amartindalonsoc.groover.utils.UserPlaylistsAdapter
import kotlinx.android.synthetic.main.fragment_recommendation.*
import kotlinx.android.synthetic.main.playlist_fragment.*
//import com.amartindalonsoc.groover.ui.login.StoredUser
//import com.android.volley.Request
//import com.android.volley.Response
//import com.android.volley.toolbox.JsonObjectRequest
//import com.google.android.material.transformation.TransformationChildLayout
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.gson.JsonObject
//import com.spotify.protocol.types.Image
//import com.spotify.protocol.types.Track
//import com.squareup.moshi.Moshi
//import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendationFragment: Fragment() {

    private lateinit var userPlaylistsLinearLayoutManager: LinearLayoutManager
    private lateinit var userPlaylistsAdapter: UserPlaylistsAdapter

    lateinit var recommendationFragmentContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recommendation, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recommendationFragmentContext = activity!!.applicationContext
        userPlaylistsLinearLayoutManager = LinearLayoutManager(recommendationFragmentContext)
        user_playlists_recycler_view.layoutManager = userPlaylistsLinearLayoutManager

        val itemsForRecommendation = mutableListOf<ItemForRecommendation>(ItemForRecommendation(false, null))
        getUserPlaylists(itemsForRecommendation)



//
//        createTopTracksLayout() //Creates the top 50 tracks layout
//
//        if (GlobalVars.playlistsArray != null) {
//            for(i in 0 until (GlobalVars.playlistsArray!!.length())){
//                val playlistJSON = JSONObject(GlobalVars.playlistsArray!![i].toString())
//                val playlistName = playlistJSON.get("name").toString()
//                val playlistId = playlistJSON.get("id").toString()
//
//                if (playlistName != ""){
//
//                    val playlistFullLayout = LinearLayout(context)
//                    val playlistFullLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//                    playlistFullLayoutParams.setMargins(0,0,0,dpToPx(5))
//                    playlistFullLayout.layoutParams = playlistFullLayoutParams
//                    playlistFullLayout.orientation = LinearLayout.VERTICAL
//
//                    val playlistInfoLayout = LinearLayout(context)
//                    val playlistInfoLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40))
//                    playlistInfoLayoutParams.setMargins(0,0,0,dpToPx(2))
//                    playlistInfoLayout.layoutParams = playlistInfoLayoutParams
//
//                    val playlistImageView = ImageView(context)
//                    val playlistImageViewParams = LinearLayout.LayoutParams(dpToPx(40),dpToPx(40))
//                    playlistImageView.layoutParams = playlistImageViewParams
//                    if (playlistJSON.getJSONArray("images").length() > 0){
//                        Picasso.get().load(
//                            JSONObject(playlistJSON.getJSONArray("images")[0].toString()).get(
//                                "url"
//                            ).toString()
//                        ).into(playlistImageView)
//                    }else{
//                        playlistImageView.setImageResource(R.drawable.no_image_playlist)
//                    }
//
//
//                    val playlistTextView = TextView(context)
//                    val playlistTextViewParams = LinearLayout.LayoutParams(dpToPx(250), LinearLayout.LayoutParams.MATCH_PARENT, 2f)
//                    playlistTextViewParams.setMargins(dpToPx(5),0,0,0)
//                    playlistTextView.layoutParams = playlistTextViewParams
//                    playlistTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
//                    playlistTextView.marqueeRepeatLimit = -1
//                    playlistTextView.setSingleLine(true)
//                    playlistTextView.isSelected = true
//                    playlistTextView.gravity = Gravity.CENTER_VERTICAL
//                    playlistTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
//                    playlistTextView.text = playlistName
//                    playlistTextView.setOnClickListener {
//                        selectPlaylist(playlistInfoLayout, playlistName, playlistId)
//                    }
//
//                    val emptyView = View(context)
//                    emptyView.layoutParams = LinearLayout.LayoutParams(0,0,1f)
//
//                    val expandButton = ImageView(context)
//                    val expandButtonParams = LinearLayout.LayoutParams(dpToPx(40),dpToPx(40))
//                    expandButtonParams.setMargins(dpToPx(5),0,0,0)
//                    expandButton.layoutParams = expandButtonParams
//                    expandButton.setImageResource(R.drawable.ic_expand_more)
//
//                    expandButton.setOnClickListener {
//                        expandSetOnClickListener(expandButton, playlistFullLayout, playlistId)
//                    }
//
//                    val playButton = ImageView(context)
//                    val playButtonParams = LinearLayout.LayoutParams(dpToPx(40),dpToPx(40))
//                    playButton.layoutParams = playButtonParams
//
//                    if (StoredUser.spotifyUser.product == "premium" && GlobalVars.spotifyInstalled){
//                        playButton.setImageResource(R.drawable.ic_play_foreground)
//                        playButton.setOnClickListener {
//                            GlobalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
//                            GlobalVars.mSpotifyAppRemote.playerApi.play("spotify:playlist:" + playlistId)
//                        }
//                    }
//
//                    playlistInfoLayout.addView(playlistImageView)
//                    playlistInfoLayout.addView(playlistTextView)
//                    playlistInfoLayout.addView(emptyView)
//                    playlistInfoLayout.addView(playButton)
//                    playlistInfoLayout.addView(expandButton)
//
//                    playlistFullLayout.addView(playlistInfoLayout)
//
//                    playlists_listed.addView(playlistFullLayout)
//                }
//            }
//        }
//
//
////        val playlistsRequest = object:  JsonObjectRequest(
////            Request.Method.GET, "https://api.spotify.com/v1/me/playlists?limit=50", null,
////            Response.Listener<JSONObject>{ jsonResponse ->
////                val playlistsArray = jsonResponse.getJSONArray("items")
////                for(i in 0 until (playlistsArray.length())){
////                    val playlistJSON = JSONObject(playlistsArray[i].toString())
////                    val playlistName = playlistJSON.get("name").toString()
////                    val playlistId = playlistJSON.get("id").toString()
////
////                    if (playlistName != ""){
////
////                        val playlistFullLayout = LinearLayout(context)
////                        val playlistFullLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
////                        playlistFullLayoutParams.setMargins(0,0,0,dpToPx(5))
////                        playlistFullLayout.layoutParams = playlistFullLayoutParams
////                        playlistFullLayout.orientation = LinearLayout.VERTICAL
////
////                        val playlistInfoLayout = LinearLayout(context)
////                        val playlistInfoLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40))
////                        playlistInfoLayoutParams.setMargins(0,0,0,dpToPx(2))
////                        playlistInfoLayout.layoutParams = playlistInfoLayoutParams
////
////                        val playlistImageView = ImageView(context)
////                        val playlistImageViewParams = LinearLayout.LayoutParams(dpToPx(40),dpToPx(40))
////                        playlistImageView.layoutParams = playlistImageViewParams
////                        if (playlistJSON.getJSONArray("images").length() > 0){
////                            Picasso.get().load(
////                                JSONObject(playlistJSON.getJSONArray("images")[0].toString()).get(
////                                    "url"
////                                ).toString()
////                            ).into(playlistImageView)
////                        }else{
////                            playlistImageView.setImageResource(R.drawable.no_image_playlist)
////                        }
////
////
////                        val playlistTextView = TextView(context)
////                        val playlistTextViewParams = LinearLayout.LayoutParams(dpToPx(250), LinearLayout.LayoutParams.MATCH_PARENT, 2f)
////                        playlistTextViewParams.setMargins(dpToPx(5),0,0,0)
////                        playlistTextView.layoutParams = playlistTextViewParams
////                        playlistTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
////                        playlistTextView.marqueeRepeatLimit = -1
////                        playlistTextView.setSingleLine(true)
////                        playlistTextView.isSelected = true
////                        playlistTextView.gravity = Gravity.CENTER_VERTICAL
////                        playlistTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
////                        playlistTextView.text = playlistName
////                        playlistTextView.setOnClickListener {
////                            selectPlaylist(playlistInfoLayout, playlistName, playlistId)
////                        }
////
////                        val emptyView = View(context)
////                        emptyView.layoutParams = LinearLayout.LayoutParams(0,0,1f)
////
////                        val expandButton = ImageView(context)
////                        val expandButtonParams = LinearLayout.LayoutParams(dpToPx(40),dpToPx(40))
////                        expandButtonParams.setMargins(dpToPx(5),0,0,0)
////                        expandButton.layoutParams = expandButtonParams
////                        expandButton.setImageResource(R.drawable.ic_expand_more)
////
////                        expandButton.setOnClickListener {
////                            expandSetOnClickListener(expandButton, playlistFullLayout, playlistId)
////                        }
////
////                        val playButton = ImageView(context)
////                        val playButtonParams = LinearLayout.LayoutParams(dpToPx(40),dpToPx(40))
////                        playButton.layoutParams = playButtonParams
////
////                        if (StoredUser.spotifyUser.product == "premium" && GlobalVars.spotifyInstalled){
////                            playButton.setImageResource(R.drawable.ic_play_foreground)
////                            playButton.setOnClickListener {
////                                GlobalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
////                                GlobalVars.mSpotifyAppRemote.playerApi.play("spotify:playlist:" + playlistId)
////                            }
////                        }
////
////                        playlistInfoLayout.addView(playlistImageView)
////                        playlistInfoLayout.addView(playlistTextView)
////                        playlistInfoLayout.addView(emptyView)
////                        playlistInfoLayout.addView(playButton)
////                        playlistInfoLayout.addView(expandButton)
////
////                        playlistFullLayout.addView(playlistInfoLayout)
////
////                        playlists_listed.addView(playlistFullLayout)
////                    }
////                }
////
////            },
////            Response.ErrorListener { error ->}){
////
////            override fun getHeaders(): MutableMap<String, String> {
////                val requestHeader: MutableMap<String,String> = hashMapOf()
////                requestHeader.put("Accept","application/json")
////                requestHeader.put("Content-Type","application/json")
////                requestHeader.put("Authorization","Bearer " + StoredUser.access)
////
////                return requestHeader
////            }
////        }
////        MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(playlistsRequest)
//
//        getRecommendationButton.setOnClickListener {
//            //TODO Sustituir este Toast por hacer la recomendacion
//            Toast.makeText(activity!!.applicationContext, "Ir a recomendacion", Toast.LENGTH_SHORT).show()
//            getVectors()
//        }
//
    }

    fun getTopTracks(itemForRecommendation: MutableList<ItemForRecommendation>) {
        val requestForTopTracks = Api.spotifyApiRequest()
        val userToken = SharedPreferencesManager.getString(Constants.spotify_user_token, recommendationFragmentContext)!!
        val callForTopTracks = requestForTopTracks.getTopTracks(Constants.short_term, 50, ("Bearer " + userToken))
        callForTopTracks.enqueue(object : Callback<SpotifyTopTracksResponse> {

            override fun onResponse(call: Call<SpotifyTopTracksResponse>, response: Response<SpotifyTopTracksResponse>) {
                if (response.isSuccessful) {
                    println("success")
                    if (response.body() != null) {
                        for (track in response.body()!!.items) { //TODO Hacer check de body no vacio y pasarlo al adapter que corresponda
                            Log.i("TOP_TRACKS", track.name)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<SpotifyTopTracksResponse>, t: Throwable) {
                Log.i("CallbackFailure", t.message)
            }
        })
    }

    fun getUserPlaylists(itemForRecommendation: MutableList<ItemForRecommendation>) {
        val requestForUserPlaylists = Api.spotifyApiRequest()
        val userId = SharedPreferencesManager.getString(Constants.spotify_user_id, recommendationFragmentContext)!!
        val userToken = SharedPreferencesManager.getString(Constants.spotify_user_token, recommendationFragmentContext)!!
        val callForUserPlaylists = requestForUserPlaylists.getUserPlaylists(userId, ("Bearer " + userToken))
        callForUserPlaylists.enqueue(object : Callback<SpotifyUserPlaylistsResponse> {

            override fun onResponse(call: Call<SpotifyUserPlaylistsResponse>, response: Response<SpotifyUserPlaylistsResponse>) {
                if (response.isSuccessful) {
                    for (playlist in response.body()!!.items) { //TODO Hacer check de body no vacio y pasarlo al adapter que corresponda
                        Log.i("USER_PLAYLIST", playlist.name)
                        val item = ItemForRecommendation(true,playlist)
                        itemForRecommendation.add(item)
                    }

                    userPlaylistsAdapter = UserPlaylistsAdapter(itemForRecommendation)
                    user_playlists_recycler_view.adapter = userPlaylistsAdapter
//                    setRecyclerViewScrollListener()
                }
            }

            override fun onFailure(call: Call<SpotifyUserPlaylistsResponse>, t: Throwable) {
                Log.i("CallbackFailure", t.message)
            }
        })
    }

    private fun setRecyclerViewScrollListener() {
        user_playlists_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
            }
        })
    }
//
//
//
//    fun selectPlaylist(playlistInfoLayout: LinearLayout, playlistName: String, playlistUrl: String){
//        if (anySelected){
//            if (playlistInfoLayout == otherLayoutColored){
//                playlistInfoLayout.setBackgroundColor(Color.WHITE)
//                anySelected = false
//                selectedPlaylistId = ""
//
//                getRecommendationButton.isClickable = false
//                getRecommendationButton.text = "Select a playlist"
//            }else{
//                otherLayoutColored.setBackgroundColor(Color.WHITE)
//
//                playlistInfoLayout.setBackgroundColor(ContextCompat.getColor(activity!!.applicationContext,R.color.selected_playlist))
//                otherLayoutColored = playlistInfoLayout
//                selectedPlaylistId = playlistUrl
//
//                getRecommendationButton.isClickable = true
//                getRecommendationButton.text = "Use \"" + playlistName + "\" to get recommendations"
//            }
//        }else{
//            playlistInfoLayout.setBackgroundColor(ContextCompat.getColor(activity!!.applicationContext,R.color.selected_playlist))
//            otherLayoutColored = playlistInfoLayout
//            anySelected = true
//            selectedPlaylistId = playlistUrl
//
//            getRecommendationButton.isClickable = true
//            getRecommendationButton.text = "Use \"" + playlistName + "\" to get recommendations"
//        }
//    }
//
//
//
//
//    fun expandSetOnClickListener(clickedImage: ImageView, playlistFullLayout: LinearLayout, playlistId: String){
//        if (anyExpanded){
//            otherExpandedImage.setImageResource(R.drawable.ic_expand_more)
//            val parent = otherLinearLayout.parent as ViewGroup
//            parent.removeView(otherLinearLayout)
//
//            if (clickedImage == otherExpandedImage){
//                anyExpanded = false
//            }else{
//                clickedImage.setImageResource(R.drawable.ic_expand_less)
//                otherExpandedImage = clickedImage
//
//                val playlistContent = LinearLayout(context)
//                playlistContent.orientation = LinearLayout.VERTICAL
//
//                val playlistRequest = object : JsonObjectRequest(
//                    Method.GET,
//                    "https://api.spotify.com/v1/playlists/" + playlistId + "?fields=name%2Cimages%2Ctracks.items(track(name%2Chref%2Curi%2Cartists(name)%2Calbum(name%2Cimages)))",
//                    null,
//                    Response.Listener<JSONObject> { jsonResponse ->
//                        val itemsArray =
//                            jsonResponse.getJSONObject("tracks").getJSONArray("items")
//                        val moshi = Moshi.Builder().build()
//                        val jsonAdapter = moshi.adapter(Track::class.java)
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
//                            val songView = LinearLayout(context)
//                            val songViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(35))
//                            songViewParams.setMargins(0, 0, 0, dpToPx(1))
//                            songView.orientation = LinearLayout.HORIZONTAL
//                            songView.layoutParams = songViewParams
//
//                            val songImageView = ImageView(context)
//                            val songImageViewParams = LinearLayout.LayoutParams(dpToPx(35), dpToPx(35))
//                            songImageViewParams.setMargins(dpToPx(5), 0, 0, 0)
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
//                            val songTextViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
//                            songTextViewParams.setMargins(dpToPx(5), 0, 0, 0)
//                            songTextView.layoutParams = songTextViewParams
//                            songTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
//                            songTextView.marqueeRepeatLimit = -1
//                            songTextView.setSingleLine(true)
//                            songTextView.isSelected = true
//                            songTextView.gravity = Gravity.CENTER_VERTICAL
//                            songTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//                            songTextView.text = song.name + " [" + song.artists[0].name + "]"
//
//                            songView.addView(songImageView)
//                            songView.addView(songTextView)
//
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
//                MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(playlistRequest)
//
//                otherLinearLayout = playlistContent
//
//                playlistFullLayout.addView(playlistContent)
//            }
//
//        }else{
//            clickedImage.setImageResource(R.drawable.ic_expand_less)
//            otherExpandedImage = clickedImage
//
//            val playlistContent = LinearLayout(context)
//            playlistContent.orientation = LinearLayout.VERTICAL
//
//            val playlistRequest = object : JsonObjectRequest(
//                Method.GET,
//                "https://api.spotify.com/v1/playlists/" + playlistId + "?fields=name%2Cimages%2Ctracks.items(track(name%2Chref%2Curi%2Cartists(name)%2Calbum(name%2Cimages)))",
//                null,
//                Response.Listener<JSONObject> { jsonResponse ->
//                    val itemsArray =
//                        jsonResponse.getJSONObject("tracks").getJSONArray("items")
//                    val moshi = Moshi.Builder().build()
//                    val jsonAdapter = moshi.adapter(Track::class.java)
//
//                    val plt = Playlist()
//                    for (i in 0 until (itemsArray.length())) {
//                        val track = jsonAdapter.fromJson(
//                            JSONObject(itemsArray[i].toString()).getJSONObject("track").toString()
//                        )
//                        if (track != null) {
//                            plt.tracks.add(track)
//                        }
//                    }
//
//                    for (song in plt.tracks) {
//                        val songView = LinearLayout(context)
//                        val songViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(35))
//                        songViewParams.setMargins(0, 0, 0, dpToPx(1))
//                        songView.orientation = LinearLayout.HORIZONTAL
//                        songView.layoutParams = songViewParams
//
//                        val songImageView = ImageView(context)
//                        val songImageViewParams = LinearLayout.LayoutParams(dpToPx(35), dpToPx(35))
//                        songImageViewParams.setMargins(dpToPx(5), 0, 0, 0)
//                        songImageView.layoutParams = songImageViewParams
//
//                        if (StoredUser.spotifyUser.product == "premium" && GlobalVars.spotifyInstalled){
//                            songImageView.setBackgroundResource(R.drawable.ic_play_foreground)
//                            songImageView.setOnClickListener {
//                                GlobalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
//                                GlobalVars.mSpotifyAppRemote.playerApi.play(song.uri)
//                            }
//                        }
//
//
//
//                        val songTextView = TextView(context)
//                        val songTextViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
//                        songTextViewParams.setMargins(dpToPx(5), 0, 0, 0)
//                        songTextView.layoutParams = songTextViewParams
//                        songTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
//                        songTextView.marqueeRepeatLimit = -1
//                        songTextView.setSingleLine(true)
//                        songTextView.isSelected = true
//                        songTextView.gravity = Gravity.CENTER_VERTICAL
//                        songTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//                        songTextView.text = song.name + " [" + song.artists[0].name + "]"
//
//                        songView.addView(songImageView)
//                        songView.addView(songTextView)
//
//                        playlistContent.addView(songView)
//
//                    }
//
//                },
//                Response.ErrorListener { error -> }) {
//
//                override fun getHeaders(): MutableMap<String, String> {
//                    val requestHeader: MutableMap<String, String> = hashMapOf()
//                    requestHeader.put("Accept", "application/json")
//                    requestHeader.put("Content-Type", "application/json")
//                    requestHeader.put("Authorization", "Bearer " + StoredUser.access)
//
//                    return requestHeader
//                }
//            }
//
//
//            MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(playlistRequest)
//
//
//            otherLinearLayout = playlistContent
//
//            playlistFullLayout.addView(playlistContent)
//
//            anyExpanded = true
//        }
//    }
//
//    fun expandTopSetOnCliclListener(clickedImage: ImageView, playlistFullLayout: LinearLayout){
//        if (anyExpanded){
//            otherExpandedImage.setImageResource(R.drawable.ic_expand_more)
//            val parent = otherLinearLayout.parent as ViewGroup
//            parent.removeView(otherLinearLayout)
//
//            if (clickedImage == otherExpandedImage){
//                anyExpanded = false
//            }else{
//                clickedImage.setImageResource(R.drawable.ic_expand_less)
//                otherExpandedImage = clickedImage
//
//                val topContent = LinearLayout(context)
//                topContent.orientation = LinearLayout.VERTICAL
//
//                val playlistRequest = object : JsonObjectRequest(
//                    Method.GET,
//                    "https://api.spotify.com/v1/me/top/tracks?time_range=short_term&limit=50",
//                    null,
//                    Response.Listener<JSONObject> { jsonResponse ->
//                        val itemsArray = jsonResponse.getJSONArray("items")
//                        val moshi = Moshi.Builder().build()
//                        val jsonAdapter = moshi.adapter(Track::class.java)
//
//                        val plt = Playlist()
//                        for (i in 0 until (itemsArray.length())) {
//                            val track = jsonAdapter.fromJson(itemsArray[i].toString())
//                            if (track != null) {
//                                plt.tracks.add(track)
//                            }
//                        }
//
//                        for (song in plt.tracks) {
//                            val songView = LinearLayout(context)
//                            val songViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(35))
//                            songViewParams.setMargins(0, 0, 0, dpToPx(1))
//                            songView.orientation = LinearLayout.HORIZONTAL
//                            songView.layoutParams = songViewParams
//
//                            val songImageView = ImageView(context)
//                            val songImageViewParams = LinearLayout.LayoutParams(dpToPx(35), dpToPx(35))
//                            songImageViewParams.setMargins(dpToPx(5), 0, 0, 0)
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
//                            val songTextViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
//                            songTextViewParams.setMargins(dpToPx(5), 0, 0, 0)
//                            songTextView.layoutParams = songTextViewParams
//                            songTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
//                            songTextView.marqueeRepeatLimit = -1
//                            songTextView.setSingleLine(true)
//                            songTextView.isSelected = true
//                            songTextView.gravity = Gravity.CENTER_VERTICAL
//                            songTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//                            songTextView.text = song.name + " [" + song.artists[0].name + "]"
//
//                            songView.addView(songImageView)
//                            songView.addView(songTextView)
//
//                            topContent.addView(songView)
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
//                MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(playlistRequest)
//
//                otherLinearLayout = topContent
//
//                playlistFullLayout.addView(topContent)
//            }
//
//        }else{
//            clickedImage.setImageResource(R.drawable.ic_expand_less)
//            otherExpandedImage = clickedImage
//
//            val topContent = LinearLayout(context)
//            topContent.orientation = LinearLayout.VERTICAL
//
//            val playlistRequest = object : JsonObjectRequest(
//                Method.GET,
//                "https://api.spotify.com/v1/me/top/tracks?time_range=short_term&limit=50",
//                null,
//                Response.Listener<JSONObject> { jsonResponse ->
//                    val itemsArray = jsonResponse.getJSONArray("items")
//                    val moshi = Moshi.Builder().build()
//                    val jsonAdapter = moshi.adapter(Track::class.java)
//
//                    val plt = Playlist()
//                    for (i in 0 until (itemsArray.length())) {
//                        val track = jsonAdapter.fromJson(itemsArray[i].toString())
//                        if (track != null) {
//                            plt.tracks.add(track)
//                        }
//                    }
//
//                    for (song in plt.tracks) {
//                        val songView = LinearLayout(context)
//                        val songViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(35))
//                        songViewParams.setMargins(0, 0, 0, dpToPx(1))
//                        songView.orientation = LinearLayout.HORIZONTAL
//                        songView.layoutParams = songViewParams
//
//                        val songImageView = ImageView(context)
//                        val songImageViewParams = LinearLayout.LayoutParams(dpToPx(35), dpToPx(35))
//                        songImageViewParams.setMargins(dpToPx(5), 0, 0, 0)
//                        songImageView.layoutParams = songImageViewParams
//
//                        if (StoredUser.spotifyUser.product == "premium" && GlobalVars.spotifyInstalled){
//                            songImageView.setBackgroundResource(R.drawable.ic_play_foreground)
//                            songImageView.setOnClickListener {
//                                GlobalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
//                                GlobalVars.mSpotifyAppRemote.playerApi.play(song.uri)
//                            }
//                        }
//
//
//
//                        val songTextView = TextView(context)
//                        val songTextViewParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
//                        songTextViewParams.setMargins(dpToPx(5), 0, 0, 0)
//                        songTextView.layoutParams = songTextViewParams
//                        songTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
//                        songTextView.marqueeRepeatLimit = -1
//                        songTextView.setSingleLine(true)
//                        songTextView.isSelected = true
//                        songTextView.gravity = Gravity.CENTER_VERTICAL
//                        songTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
//                        songTextView.text = song.name + " [" + song.artists[0].name + "]"
//
//                        songView.addView(songImageView)
//                        songView.addView(songTextView)
//
//                        topContent.addView(songView)
//
//                    }
//
//                },
//                Response.ErrorListener { error -> }) {
//
//                override fun getHeaders(): MutableMap<String, String> {
//                    val requestHeader: MutableMap<String, String> = hashMapOf()
//                    requestHeader.put("Accept", "application/json")
//                    requestHeader.put("Content-Type", "application/json")
//                    requestHeader.put("Authorization", "Bearer " + StoredUser.access)
//
//                    return requestHeader
//                }
//            }
//
//
//            MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(playlistRequest)
//
//
//            otherLinearLayout = topContent
//
//            playlistFullLayout.addView(topContent)
//
//            anyExpanded = true
//        }
//    }
//
//    fun createTopTracksLayout(){
//        val topFullLayout = LinearLayout(context)
//        val topFullLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//        topFullLayoutParams.setMargins(0,0,0,dpToPx(5))
//        topFullLayout.layoutParams = topFullLayoutParams
//        topFullLayout.orientation = LinearLayout.VERTICAL
//
//        val topInfoLayout = LinearLayout(context)
//        val topInfoLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dpToPx(40))
//        topInfoLayoutParams.setMargins(0,0,0,dpToPx(2))
//        topInfoLayout.layoutParams = topInfoLayoutParams
//
//        val topImageView = ImageView(context) //TODO Ponerle fondo a la imagen para que al elegir esto para recomendacion no se ponga el fondo de la estrella en verde
//        val topImageViewParams = LinearLayout.LayoutParams(dpToPx(40),dpToPx(40))
//        topImageView.layoutParams = topImageViewParams
//        topImageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
//        topImageView.setImageResource(R.drawable.ic_top_tracks)
//
//
//        val topTextView = TextView(context)
//        val topTextViewParams = LinearLayout.LayoutParams(dpToPx(250), LinearLayout.LayoutParams.MATCH_PARENT, 2f)
//        topTextViewParams.setMargins(dpToPx(5),0,0,0)
//        topTextView.layoutParams = topTextViewParams
//        topTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
//        topTextView.marqueeRepeatLimit = -1
//        topTextView.setSingleLine(true)
//        topTextView.isSelected = true
//        topTextView.gravity = Gravity.CENTER_VERTICAL
//        topTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
//        topTextView.text = "Top 50 tracks"
//        topTextView.setOnClickListener {
//            selectPlaylist(topInfoLayout, "Top 50 tracks", "topTracks")
//        }
//
//        val emptyView = View(context)
//        emptyView.layoutParams = LinearLayout.LayoutParams(0,0,1f)
//
//        val expandButton = ImageView(context)
//        val expandButtonParams = LinearLayout.LayoutParams(dpToPx(40),dpToPx(40))
//        expandButtonParams.setMargins(dpToPx(5),0,0,0)
//        expandButton.layoutParams = expandButtonParams
//        expandButton.setImageResource(R.drawable.ic_expand_more)
//
//        expandButton.setOnClickListener {
//            expandTopSetOnCliclListener(expandButton, topFullLayout)
//        }
//
//        val playButton = ImageView(context)
//        val playButtonParams = LinearLayout.LayoutParams(dpToPx(40),dpToPx(40))
//        playButton.layoutParams = playButtonParams
//        //TODO Reproducir el top 50
//        /*if (storedUser.spotifyUser.product == "premium" && globalVars.spotifyInstalled){
//            playButton.setImageResource(R.drawable.ic_play_foreground)
//            playButton.setOnClickListener {
//                globalVars.mSpotifyAppRemote.connectApi.connectSwitchToLocalDevice()
//                globalVars.mSpotifyAppRemote.playerApi.play("spotify:playlist:" + playlistId)
//            }
//        }*/
//
//        topInfoLayout.addView(topImageView)
//        topInfoLayout.addView(topTextView)
//        topInfoLayout.addView(emptyView)
//        topInfoLayout.addView(playButton)
//        topInfoLayout.addView(expandButton)
//
//        topFullLayout.addView(topInfoLayout)
//
//        playlists_listed.addView(topFullLayout)
//    }
//
//
//    fun getVectors(){
//
//        var tracksString = ""
//        var numOfTracks = 0
//        var acousticnessSum = 0.0
//        var danceabilitySum = 0.0
//        var energySum = 0.0
//        var instrumentalnessSum = 0.0
//        var speechinessSum = 0.0
//        var tempoSum = 0.0
//        var valenceSum = 0.0
//
//        if (selectedPlaylistId == "topTracks"){
//            val playlistRequest = object : JsonObjectRequest(
//            Method.GET,
//            "https://api.spotify.com/v1/me/top/tracks?time_range=short_term&limit=50",
//            null,
//            Response.Listener<JSONObject> { jsonResponse ->
//                val tracksArray = jsonResponse.getJSONArray("items")
//
//                for (i in 0 until (tracksArray.length())) {
//                    val trackId = JSONObject(tracksArray[i].toString()).getString("id")
//                    if (i < tracksArray.length() -1){
//                        tracksString +=  trackId + "%2C"
//                    }else{
//                        tracksString += trackId
//                    }
//                }
//
//                val audioFeaturesRequest = object : JsonObjectRequest(
//                    Method.GET,
//                    "https://api.spotify.com/v1/audio-features?ids=" + tracksString,
//                    null,
//                    Response.Listener<JSONObject> { jsonResponse ->
//                        val audioFeaturesArray = jsonResponse.getJSONArray("audio_features")
//                        val moshi = Moshi.Builder().build()
//                        val jsonAdapter = moshi.adapter(AudioFeatures::class.java)
//                        numOfTracks = audioFeaturesArray.length()
//
//                        for (i in 0 until (numOfTracks)) {
//                            val trackAudioFeatures = jsonAdapter.fromJson(audioFeaturesArray[i].toString())
//                            if (trackAudioFeatures != null){
//                                acousticnessSum += trackAudioFeatures!!.acousticness
//                                danceabilitySum += trackAudioFeatures!!.danceability
//                                energySum += trackAudioFeatures!!.energy
//                                instrumentalnessSum += trackAudioFeatures!!.instrumentalness
//                                speechinessSum += trackAudioFeatures!!.speechiness
//                                tempoSum += trackAudioFeatures!!.tempo
//                                valenceSum += trackAudioFeatures!!.valence
//                            }else{
//                                numOfTracks -= 1
//                            }
//                        }
//
//                        getRecomendation(
//                            acousticnessSum/numOfTracks,
//                            danceabilitySum/numOfTracks,
//                            energySum/numOfTracks,
//                            instrumentalnessSum/numOfTracks,
//                            speechinessSum/numOfTracks,
//                            tempoSum/numOfTracks,
//                            valenceSum/numOfTracks
//                        )
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
//                MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(audioFeaturesRequest)
//
//            },
//            Response.ErrorListener { error -> }) {
//
//            override fun getHeaders(): MutableMap<String, String> {
//                val requestHeader: MutableMap<String, String> = hashMapOf()
//                requestHeader.put("Accept", "application/json")
//                requestHeader.put("Content-Type", "application/json")
//                requestHeader.put("Authorization", "Bearer " + StoredUser.access)
//
//                return requestHeader
//            }
//        }
//            MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(playlistRequest)
//
//        }else{
//            val playlistRequest = object : JsonObjectRequest(
//                Method.GET,
//                "https://api.spotify.com/v1/playlists/" + selectedPlaylistId + "/tracks?fields=items(track(id))",
//                null,
//                Response.Listener<JSONObject> { jsonResponse ->
//                    val tracksArray = jsonResponse.getJSONArray("items")
//
//                    for (i in 0 until (tracksArray.length())) {
//                        val trackId = JSONObject(tracksArray[i].toString()).getJSONObject("track").get("id").toString()
//                        if (i < tracksArray.length() -1){
//                            tracksString +=  trackId + "%2C"
//                        }else{
//                            tracksString += trackId
//                        }
//                    }
//
//                    val audioFeaturesRequest = object : JsonObjectRequest(
//                        Method.GET,
//                        "https://api.spotify.com/v1/audio-features?ids=" + tracksString,
//                        null,
//                        Response.Listener<JSONObject> { jsonResponse ->
//                            val audioFeaturesArray = jsonResponse.getJSONArray("audio_features")
//                            val moshi = Moshi.Builder().build()
//                            val jsonAdapter = moshi.adapter(AudioFeatures::class.java)
//                            numOfTracks = audioFeaturesArray.length()
//
//                            for (i in 0 until (numOfTracks)) {
//                                val trackAudioFeatures = jsonAdapter.fromJson(audioFeaturesArray[i].toString())
//                                if (trackAudioFeatures != null){
//                                    acousticnessSum += trackAudioFeatures!!.acousticness
//                                    danceabilitySum += trackAudioFeatures!!.danceability
//                                    energySum += trackAudioFeatures!!.energy
//                                    instrumentalnessSum += trackAudioFeatures!!.instrumentalness
//                                    speechinessSum += trackAudioFeatures!!.speechiness
//                                    tempoSum += trackAudioFeatures!!.tempo
//                                    valenceSum += trackAudioFeatures!!.valence
//                                }else{
//                                    numOfTracks -= 1
//                                }
//                            }
//
//                            getRecomendation(
//                                acousticnessSum/numOfTracks,
//                                danceabilitySum/numOfTracks,
//                                energySum/numOfTracks,
//                                instrumentalnessSum/numOfTracks,
//                                speechinessSum/numOfTracks,
//                                tempoSum/numOfTracks,
//                                valenceSum/numOfTracks
//                            )
//
//                        },
//                        Response.ErrorListener { error -> }) {
//
//                        override fun getHeaders(): MutableMap<String, String> {
//                            val requestHeader: MutableMap<String, String> = hashMapOf()
//                            requestHeader.put("Accept", "application/json")
//                            requestHeader.put("Content-Type", "application/json")
//                            requestHeader.put("Authorization", "Bearer " + StoredUser.access)
//
//                            return requestHeader
//                        }
//                    }
//                    MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(audioFeaturesRequest)
//
//                },
//                Response.ErrorListener { error -> }) {
//
//                override fun getHeaders(): MutableMap<String, String> {
//                    val requestHeader: MutableMap<String, String> = hashMapOf()
//                    requestHeader.put("Accept", "application/json")
//                    requestHeader.put("Content-Type", "application/json")
//                    requestHeader.put("Authorization", "Bearer " + StoredUser.access)
//
//                    return requestHeader
//                }
//            }
//            MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(playlistRequest)
//        }
//
//    }
//
//
//    fun getRecomendation(acousticnessAvgUser: Double, danceabilityAvgUser: Double, energyAvgUser: Double, instrumentalnessAvgUser: Double, speechinessAvgUser: Double, tempoAvgUser: Double, valenceAvgUser: Double){
//        val similarityMap = mutableMapOf<Place,Double>()
//
//
//        for (i in 0 until (places.size)){
//            val place = places[i]
//            var similarity = 0.0
//
//            if (place.playlist["id"] != ""){
//
//                var tracksString = ""
//                var numOfTracks = 0
//
//                var acousticnessSum = 0.0
//                var danceabilitySum = 0.0
//                var energySum = 0.0
//                var instrumentalnessSum = 0.0
//                var speechinessSum = 0.0
//                var tempoSum = 0.0
//                var valenceSum = 0.0
//
//
//                val playlistRequest = object : JsonObjectRequest(
//                    Method.GET,
//                    "https://api.spotify.com/v1/playlists/" + place.playlist + "/tracks?fields=items(track(id))",
//                    null,
//                    Response.Listener<JSONObject> { jsonResponse ->
//                        val tracksArray = jsonResponse.getJSONArray("items")
//
//                        for (t in 0 until (tracksArray.length())) {
//                            val trackId = JSONObject(tracksArray[t].toString()).getJSONObject("track").get("id").toString()
//                            if (t < tracksArray.length() -1){
//                                tracksString +=  trackId + "%2C"
//                            }else{
//                                tracksString += trackId
//                            }
//                        }
//
//                        val audioFeaturesRequest = object : JsonObjectRequest(
//                            Method.GET,
//                            "https://api.spotify.com/v1/audio-features?ids=" + tracksString,
//                            null,
//                            Response.Listener<JSONObject> { jsonResponse ->
//                                val audioFeaturesArray = jsonResponse.getJSONArray("audio_features")
//                                val moshi = Moshi.Builder().build()
//                                val jsonAdapter = moshi.adapter(AudioFeatures::class.java)
//                                numOfTracks = audioFeaturesArray.length()
//
//                                for (a in 0 until (numOfTracks)) {
//                                    val trackAudioFeatures = jsonAdapter.fromJson(audioFeaturesArray[a].toString())
//                                    if (trackAudioFeatures != null){
//                                        acousticnessSum += trackAudioFeatures!!.acousticness
//                                        danceabilitySum += trackAudioFeatures!!.danceability
//                                        energySum += trackAudioFeatures!!.energy
//                                        instrumentalnessSum += trackAudioFeatures!!.instrumentalness
//                                        speechinessSum += trackAudioFeatures!!.speechiness
//                                        tempoSum += trackAudioFeatures!!.tempo
//                                        valenceSum += trackAudioFeatures!!.valence
//                                    }else{
//                                        numOfTracks -= 1
//                                    }
//                                }
//
//                                val acousticnessAvgPlace =  acousticnessSum/numOfTracks
//                                val danceabilityAvgPlace = danceabilitySum/numOfTracks
//                                val energyAvgPlace = energySum/numOfTracks
//                                val instrumentalnessAvgPlace = instrumentalnessSum/numOfTracks
//                                val speechinessAvgPlace = speechinessSum/numOfTracks
//                                val tempoAvgPlace = tempoSum/numOfTracks
//                                val valenceAvgPlace = valenceSum/numOfTracks
//
//                                val vectorProduct = (acousticnessAvgPlace * acousticnessAvgUser) + (danceabilityAvgPlace * danceabilityAvgUser) + (energyAvgPlace * energyAvgUser) + (instrumentalnessAvgPlace * instrumentalnessAvgUser) +
//                                                    (speechinessAvgPlace * speechinessAvgUser) + (tempoAvgPlace * tempoAvgUser) + (valenceAvgPlace * valenceAvgUser)
//
//                                val placeVectorModule = sqrt( (acousticnessAvgPlace.pow(2) + danceabilityAvgPlace.pow(2) + energyAvgPlace.pow(2) + instrumentalnessAvgPlace.pow(2) + speechinessAvgPlace.pow(2) +
//                                                                tempoAvgPlace.pow(2) + valenceAvgPlace.pow(2)) )
//
//                                val userVectorModule = sqrt( (acousticnessAvgUser.pow(2) + danceabilityAvgUser.pow(2) + energyAvgUser.pow(2) + instrumentalnessAvgUser.pow(2) + speechinessAvgUser.pow(2) +
//                                                                tempoAvgUser.pow(2) + valenceAvgUser.pow(2)) )
//
//                                similarity = (vectorProduct) / ((placeVectorModule) * (userVectorModule))
//
//                                similarityMap.put(place, similarity)
//
//
//                                if (i == places.size -1){
//                                    GlobalVars.recommendedPlaces = similarityMap
//
//                                    //TODO Ver por que aqui da el size correcto del map, pero en el print a veces sale uno menos
////                                    println(places.size.toString() + " ----- " + i.toString())
////                                    for(p in similarityMap){
////                                        println(p.key.name + " ----------> " + p.value)
////                                    }
//
//                                    val recommendedPlacesFragment = RecommendedPlacesFragment()
//                                    fragmentManager?.beginTransaction()?.replace(R.id.fragment_container, recommendedPlacesFragment)?.commit()
//                                }
//
//
//
//                            },
//                            Response.ErrorListener { error -> }) {
//
//                            override fun getHeaders(): MutableMap<String, String> {
//                                val requestHeader: MutableMap<String, String> = hashMapOf()
//                                requestHeader.put("Accept", "application/json")
//                                requestHeader.put("Content-Type", "application/json")
//                                requestHeader.put("Authorization", "Bearer " + StoredUser.access)
//
//                                return requestHeader
//                            }
//                        }
//                        MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(audioFeaturesRequest)
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
//                MySingleton.getInstance(activity!!.applicationContext).addToRequestQueue(playlistRequest)
//
//            }else{
//                similarityMap.put(place, similarity)
//            }
//
//        }
//    }
//
//    ///////////////////////////////////////////////////////
//    fun dpToPx(dp: Int): Int {
//        val density = this.resources.displayMetrics.density
//        return round(dp * density).toInt()
//    }
}