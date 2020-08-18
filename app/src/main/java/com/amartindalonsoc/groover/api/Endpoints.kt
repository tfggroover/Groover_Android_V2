package com.amartindalonsoc.groover.api

import com.amartindalonsoc.groover.responses.Place
import com.amartindalonsoc.groover.responses.SpotifyAlbumResponse
import com.amartindalonsoc.groover.responses.SpotifyLoginCallback
import com.amartindalonsoc.groover.responses.SpotifyRefresh
import retrofit2.Call
import retrofit2.http.*

interface Endpoints {
    @GET("/Home/callback")
    fun loginCallbackSpotify(@Query("code") code: String, @Query("State") state: String, @Header("Cookie") cookie: String): Call<SpotifyLoginCallback>

    @GET("/Home/Auth")
    fun refreshLogin(@Query("refresh_token") refresh_token: String): Call<SpotifyRefresh>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("/v1/albums/{id}")
    fun getAlbumWithId(@Path("id") id: String, @Header("Authorization") token: String): Call<SpotifyAlbumResponse>

    @GET("/api/place")
    fun getPlaces(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("distance") distance: Double, @Query("page") page: Int, @Query("pageSize") pageSize: Int): Call<List<Place>>
}