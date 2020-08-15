package com.amartindalonsoc.groover.responses

//data class Places(
//    val results: List<Place>
//)

data class Place (
    val id: String,
    val address: String,
    val displayName: String,
    val location: Location,
    val mainPlaylist: MainPlaylist?,
    val weeklyPlaylists: WeeklyPlaylists?,
    val ratings: List<Rating>,
    val owners: List<User>,
    val phone: String?,
    val geohash: String,
    val recognizedMusic: Map<String, Long>,
    val timetables: List<Timetable>
)

data class Location (
    val latitude: Double,
    val longitude: Double
)

data class MainPlaylist (
    val id: String,
    val hash: String,
    val songs: List<Song>,
    val metrics: Any? = null, // TODO
    val snapshotVersion: String,
    val url: String
)

data class Song (
    val id: String,
    val tags: List<String>,
    val data: Map<String,String>
)

data class User (
    val born: Long,
    val displayName: String,
    val id: String,
    val currentToken: Any? = null,
    val expiresIn: Long,
    val tokenEmissionTime: String
)

data class Rating (
    val id: String,
    val value: Long,
    val user: User
)

data class Timetable (
    val id: Any? = null, // TODO
    val schedules: List<Schedule>,
    val day: Long,
    val playlist: String? = null
)

data class Schedule (
    val start: String,
    val end: String
)

data class WeeklyPlaylists (
    val monday: MainPlaylist,
    val tuesday: MainPlaylist
)