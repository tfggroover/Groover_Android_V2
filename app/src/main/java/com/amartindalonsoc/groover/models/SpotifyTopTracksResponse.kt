package com.amartindalonsoc.groover.models

import com.spotify.protocol.types.Track

data class SpotifyTopTracksResponse(
    val items: List<Track>
)