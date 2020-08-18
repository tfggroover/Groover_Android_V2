package com.amartindalonsoc.groover.responses

import com.google.gson.annotations.SerializedName

data class SpotifyRefresh (
    val spotify: SpotifyDataWithoutRefreshToken,
    val spotifyUserData: SpotifyUserData,
    val firebase: String
)

data class SpotifyDataWithoutRefreshToken (
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
    val scope: String
)