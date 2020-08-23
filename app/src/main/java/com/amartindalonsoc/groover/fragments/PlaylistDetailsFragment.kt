package com.amartindalonsoc.groover.ui.main

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.amartindalonsoc.groover.R
import com.amartindalonsoc.groover.activities.MainActivity
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.models.ItemForRecommendation
import com.amartindalonsoc.groover.models.Place
import com.amartindalonsoc.groover.models.Playlist
import com.amartindalonsoc.groover.models.SpotifyTopTracksResponse
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.PlaylistDetailsAdapter
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.amartindalonsoc.groover.utils.UserPlaylistsAdapter
import com.squareup.picasso.Picasso
//import com.amartindalonsoc.groover.ui.login.StoredUser
//import com.android.volley.Request
//import com.android.volley.Response
//import com.android.volley.toolbox.JsonObjectRequest
//import com.spotify.protocol.types.Track
//import com.squareup.moshi.Moshi
//import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.playlist_fragment.*
import kotlinx.android.synthetic.main.fragment_playlist_details.*
import kotlinx.android.synthetic.main.playlist_cell.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.round

class PlaylistDetailsFragment: Fragment() {

    private lateinit var playlistDetailsLinearLayoutManager: LinearLayoutManager
    private lateinit var playlistDetailsAdapter: PlaylistDetailsAdapter

    lateinit var playlistDetailsFragmentContext: Context
    var playlist: Playlist? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlist_details, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistDetailsFragmentContext = activity!!.applicationContext
        playlistDetailsLinearLayoutManager = LinearLayoutManager(playlistDetailsFragmentContext)
        playlist_details_recycler_view.layoutManager = playlistDetailsLinearLayoutManager

        if (playlist == null) {
            playlist_details_name.text = "Top 50 tracks recently listened"
            getTopTracks()
        } else {
            playlist_details_name.text = playlist!!.name
            if(playlist!!.images.first().url != ""){
                Picasso.get().load(playlist!!.images.first().url).into(playlist_details_image)
            } else {
                playlist_details_image.setImageResource(R.drawable.ic_profile_dark_foreground)
            }
            playlist_details_recommend_button.setOnClickListener {
                Log.i("REC_TEST", playlist!!.name)
            }
        }


    }

    fun getTopTracks() {
        val userToken = (activity as MainActivity).userToken
        val requestForTopTracks = Api.spotifyApiRequest()
        val callForTopTracks = requestForTopTracks.getTopTracks(Constants.short_term, 50, ("Bearer " + userToken))
        callForTopTracks.enqueue(object : Callback<SpotifyTopTracksResponse> {

            override fun onResponse(call: Call<SpotifyTopTracksResponse>, response: Response<SpotifyTopTracksResponse>) {
                if (response.isSuccessful) {
                    println("success")
                    if (response.body() != null) {
                        playlistDetailsAdapter = PlaylistDetailsAdapter(response.body()!!.items)
                        playlist_details_recycler_view.adapter = playlistDetailsAdapter
                    }
                }
            }

            override fun onFailure(call: Call<SpotifyTopTracksResponse>, t: Throwable) {
                Log.i("CallbackFailure", t.message)
            }
        })
    }


/*    fun getTopBasedRecommendation(): List<Place> {
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
    }*/

}