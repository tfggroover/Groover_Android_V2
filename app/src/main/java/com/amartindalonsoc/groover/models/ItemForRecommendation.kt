package com.amartindalonsoc.groover.models

data class ItemForRecommendation(
    val isPlaylist: Boolean,
    val playlist: Playlist?
)