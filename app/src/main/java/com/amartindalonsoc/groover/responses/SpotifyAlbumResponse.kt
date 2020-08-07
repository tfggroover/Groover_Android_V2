package com.amartindalonsoc.groover.responses


import com.google.gson.annotations.SerializedName

data class SpotifyAlbumResponse (
    // Poner el resto de campos que devuelve el JSON si van siendo necesarios
    val id: String,
    val images: List<SpotifyAlbumResponseImage>,
    val name: String
)

data class SpotifyAlbumResponseImage (
    val height: String,
    val width: String,
    val url: String
)