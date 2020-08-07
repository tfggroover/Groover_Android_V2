package com.amartindalonsoc.groover.api

import com.amartindalonsoc.groover.responses.Places
import com.amartindalonsoc.groover.responses.SpotifyAlbumResponse
import com.amartindalonsoc.groover.responses.SpotifyCallback
import retrofit2.Call
import retrofit2.http.*

interface Endpoints {
    @GET("/Home/callback")
    fun callbackSpotify(@Query("code") code: String, @Query("State") state: String, @Header("Cookie") cookie: String): Call<SpotifyCallback>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("/v1/albums/{id}")
    fun getAlbumWithId(@Path("id") id: String, @Header("Authorization") token: String): Call<SpotifyAlbumResponse>
}