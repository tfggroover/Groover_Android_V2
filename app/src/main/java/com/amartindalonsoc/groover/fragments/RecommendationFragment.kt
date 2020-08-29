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
import com.amartindalonsoc.groover.activities.MainActivity
import com.amartindalonsoc.groover.api.Api
import com.amartindalonsoc.groover.models.ItemForRecommendation
import com.amartindalonsoc.groover.models.SpotifyTopTracksResponse
import com.amartindalonsoc.groover.models.SpotifyUserPlaylistsResponse
import com.amartindalonsoc.groover.utils.Constants
import com.amartindalonsoc.groover.utils.SharedPreferencesManager
import com.amartindalonsoc.groover.utils.UserPlaylistsAdapter
import kotlinx.android.synthetic.main.fragment_recommendation.*
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

        getRecommendationButton.setOnClickListener {
            if ((activity as MainActivity).itemForRecommendation != null) {
                val fragment = MapFragment()
                activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
            }
        }

    }

//    fun getTopTracks(itemForRecommendation: MutableList<ItemForRecommendation>) {
//        val requestForTopTracks = Api.spotifyApiRequest()
//        val userToken = SharedPreferencesManager.getString(Constants.spotify_user_token, recommendationFragmentContext)!!
//        val callForTopTracks = requestForTopTracks.getTopTracks(Constants.short_term, 50, ("Bearer " + userToken))
//        callForTopTracks.enqueue(object : Callback<SpotifyTopTracksResponse> {
//
//            override fun onResponse(call: Call<SpotifyTopTracksResponse>, response: Response<SpotifyTopTracksResponse>) {
//                if (response.isSuccessful) {
//                    println("success")
//                    if (response.body() != null) {
//                        for (track in response.body()!!.items) { //TODO Hacer check de body no vacio y pasarlo al adapter que corresponda
//                            Log.i("TOP_TRACKS", track.name)
//                        }
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<SpotifyTopTracksResponse>, t: Throwable) {
//                Log.i("CallbackFailure", t.message)
//            }
//        })
//    }

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


                    val activityTest = (activity as MainActivity)
                    userPlaylistsAdapter = UserPlaylistsAdapter(itemForRecommendation, activityTest)
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

}