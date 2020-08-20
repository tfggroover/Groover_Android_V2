package com.amartindalonsoc.groover.models

data class SpotifyUserPlaylistsResponse(
    val items: List<Playlist>
)

data class Playlist(
    val id: String,
    val images: List<SpotifyPlaylistResponseImage>,
    val name: String
)

data class SpotifyPlaylistResponseImage (
    val height: String,
    val width: String,
    val url: String
)