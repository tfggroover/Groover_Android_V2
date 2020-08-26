package com.amartindalonsoc.groover.models

import com.spotify.protocol.types.Track

data class SpotifyPlaylistTracksResponse(
    val items: List<SpotifyPlaylistTrackObject>
)

data class SpotifyPlaylistTrackObject(
    val track: Track
)