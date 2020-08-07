package com.amartindalonsoc.groover.responses


import com.google.gson.annotations.SerializedName

data class RecognizedSong (
    val metadata: RecognizedSongMetadata,
    val status: RecognizedSongStatus
)

data class RecognizedSongMetadata (
    val music: List<RecognizedData>
)

data class RecognizedSongStatus (
    val msg: String,
    val code: Int
)

data class RecognizedData (
    val artists: List<RecognizedArtist>,
    val album: RecognizedAlbum,
    val title: String,
    @SerializedName("external_metadata")
    val externalMetadata: RecognizedExternalMetadata
)

data class RecognizedArtist (
    val name: String
)

data class RecognizedAlbum (
    val name: String
)

data class RecognizedExternalMetadata (
    val spotify: RecognizedSpotify
)

data class RecognizedSpotify (
    val track: RecognizedSpotifyTrack,
    val artists: List<RecognizedSpotifyArtist>,
    val album: RecognizedSpotifyAlbum
)

data class RecognizedSpotifyTrack (
    val name: String,
    val id: String
)

data class RecognizedSpotifyArtist (
    val name: String,
    val id: String
)

data class RecognizedSpotifyAlbum (
    val name: String,
    val id: String
)