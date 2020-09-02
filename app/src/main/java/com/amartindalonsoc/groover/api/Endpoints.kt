package com.amartindalonsoc.groover.api

import com.amartindalonsoc.groover.models.*
import com.amartindalonsoc.groover.ui.main.RecognizerFragment
import retrofit2.Call
import retrofit2.http.*

interface Endpoints {
    //Nuestra API
    @GET("/Home/callback")
    fun loginCallbackSpotify(@Query("code") code: String, @Query("State") state: String, @Header("Cookie") cookie: String): Call<SpotifyLoginCallback>

    @GET("/Home/Auth")
    fun refreshLogin(@Query("refresh_token") refresh_token: String): Call<SpotifyRefresh>

    @GET("/api/place")
    fun getPlaces(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("distance") distance: Double, @Query("page") page: Int, @Query("pageSize") pageSize: Int, @Header("Authorization") token: String): Call<List<Place>>

    @Headers("accept: */*", "Content-type: application/json")
    @POST("/api/place/{establishmentId}/song")
    fun addRecognizedSong(@Path("establishmentId") establishmentId: String, @Header("Authorization") token: String, @Body song: RecognizerFragment.RecognizedSongForBack): Call<Any>

    @GET("/api/place/recommended")
    fun getRecommendation(@Query("playlistId") playlistId: String, @Query("lat") lat: Double, @Query("lon") lon: Double, @Query("distance") distance: Double, @Query("spoToken") spoToken: String, @Query("page") page: Int, @Query("pageSize") pageSize: Int, @Header("Authorization") token: String): Call<List<Place>>

    @GET("/api/place/recommended/top")
    fun getRecommendationTop(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("distance") distance: Double, @Query("spoToken") spoToken: String, @Query("page") page: Int, @Query("pageSize") pageSize: Int, @Header("Authorization") token: String): Call<List<Place>>




    //Spotify
//    @Headers(
//        "Accept: application/json",
//        "Content-Type: application/json"
//    )
    @GET("/v1/albums/{id}")
    fun getAlbumWithId(@Path("id") id: String, @Header("Authorization") token: String): Call<SpotifyAlbumResponse>

    @GET("/v1/me/top/tracks")
    fun getTopTracks(@Query("time_range") timeRange: String, @Query("limit") limit: Int, @Header("Authorization") token: String): Call<SpotifyTopTracksResponse>

    @GET("/v1/users/{user_id}/playlists")
    fun getUserPlaylists(@Path("user_id") userId: String, @Header("Authorization") token: String): Call<SpotifyUserPlaylistsResponse>

    @GET("/v1/playlists/{playlist_id}/tracks")
    fun getPlaylistTracks(@Path("playlist_id") playlistId: String, @Header("Authorization") token: String): Call<SpotifyPlaylistTracksResponse>



}