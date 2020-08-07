package com.amartindalonsoc.groover.responses

import com.google.gson.annotations.SerializedName

data class SpotifyCallback (
    val spotify: Spotify,
    val spotifyUserData: SpotifyUserData,
    val firebase: String
)

data class Spotify (
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
    val scope: String
)

data class SpotifyUserData (
    val country: String,
    @SerializedName("display_name")
    val displayName: String,
    val email: String,
    @SerializedName("explicit_content")
    val explicitContent: ExplicitContent,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    val product: String,
    val type: String,
    val uri: String
)

data class ExplicitContent (
    @SerializedName("filter_enabled")
    val filterEnabled: Boolean,
    @SerializedName("filter_locked")
    val filterLocked: Boolean
)

data class ExternalUrls (
    val spotify: String
)

data class Image (
    val height: Any? = null,
    val width: Any? = null,
    val url: String
)
