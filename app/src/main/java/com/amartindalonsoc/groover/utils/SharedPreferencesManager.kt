package com.amartindalonsoc.groover.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.amartindalonsoc.groover.responses.Place
import com.amartindalonsoc.groover.responses.SpotifyLoginCallback
import com.amartindalonsoc.groover.responses.SpotifyRefresh
import com.google.android.gms.maps.model.LatLng

object SharedPreferencesManager {

    fun defaultPrefs(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun customPrefs(context: Context, name: String): SharedPreferences
            = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    operator fun SharedPreferences.set(key: String, value: Any?) {
        when(value) {
            is String? -> edit({ it.putString(key, value) })
            is Int -> edit({ it.putInt(key, value) })
            is Boolean -> edit({ it.putBoolean(key, value) })
            is Float -> edit({ it.putFloat(key, value) })
            is Long -> edit({ it.putLong(key, value) })
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    operator inline fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

//    fun getSharedPreferences(): SharedPreferences {
//        return context.getSharedPreferences("common", Context.MODE_PRIVATE)
//    }
//
    fun getString(name: String, context: Context): String? {
        val prefs = defaultPrefs(context)
        return prefs[name]
    }
//
//    fun saveString(name: String, value: String) {
//        getSharedPreferences().edit().putString(name,value).apply()
//    }

    fun getFloat(name: String, context: Context): Float? {
        val prefs = defaultPrefs(context)
        return prefs[name]
    }

    fun saveFloat(name: String, value: Float, context: Context) {
        val prefs = defaultPrefs(context)
        prefs[name] = value
    }

    fun getPlacesIds(context: Context): MutableSet<String>? {
        val prefs = defaultPrefs(context)
        return prefs.getStringSet("places_ids", null)
    }

    fun getMapZoom(context: Context): Float {
        val prefs = defaultPrefs(context)
        return prefs[Constants.map_zoom]!!
    }

    fun saveMapZoom(zoom: Float, context: Context) {
        val prefs = defaultPrefs(context)
        prefs[Constants.map_zoom] = zoom
    }

    fun getCameraLocation(context: Context): LatLng {
        val latitude = getFloat(Constants.camera_location_latitude, context)!!.toDouble()
        val longitude = getFloat(Constants.camera_location_longitude, context)!!.toDouble()
        return LatLng(latitude,longitude)
    }

    fun saveCameraLocation(coords: LatLng, context: Context) {
        val prefs = defaultPrefs(context)
        prefs[Constants.camera_location_latitude] = coords.latitude.toFloat()
        prefs[Constants.camera_location_longitude] = coords.longitude.toFloat()
    }

    fun saveUserFromCallback(spotifyLoginCallback: SpotifyLoginCallback, context: Context) {
        val prefs = defaultPrefs(context)
        prefs[Constants.spotify_user_token] = spotifyLoginCallback.spotify.accessToken
        prefs[Constants.spotify_refresh_token] = spotifyLoginCallback.spotify.refreshToken
        prefs[Constants.firebase_token] = spotifyLoginCallback.firebase
        prefs[Constants.user_name] = spotifyLoginCallback.spotifyUserData.displayName
        prefs[Constants.user_email] = spotifyLoginCallback.spotifyUserData.email
        prefs[Constants.user_country] = spotifyLoginCallback.spotifyUserData.country
        prefs[Constants.spotify_account_type] = spotifyLoginCallback.spotifyUserData.product
        prefs[Constants.profile_image] = spotifyLoginCallback.spotifyUserData.images.first().url //TODO Revisar si una cuenta sin imagen peta

//        saveString("firebase_token", spotifyCallback.firebase)
//        saveString("user_name", spotifyCallback.spotifyUserData.displayName)
//        saveString("user_email", spotifyCallback.spotifyUserData.email)
//        saveString("user_country", spotifyCallback.spotifyUserData.country)
//        saveString("spotify_account_type", spotifyCallback.spotifyUserData.product)
    }

    fun saveUserFromRefresh(spotifyRefresh: SpotifyRefresh, context: Context) {
        val prefs = defaultPrefs(context)
        prefs[Constants.spotify_user_token] = spotifyRefresh.spotify.accessToken
        prefs[Constants.firebase_token] = spotifyRefresh.firebase
        prefs[Constants.user_name] = spotifyRefresh.spotifyUserData.displayName
        prefs[Constants.user_email] = spotifyRefresh.spotifyUserData.email
        prefs[Constants.user_country] = spotifyRefresh.spotifyUserData.country
        prefs[Constants.spotify_account_type] = spotifyRefresh.spotifyUserData.product
        prefs[Constants.profile_image] = spotifyRefresh.spotifyUserData.images.first().url //TODO Revisar si una cuenta sin imagen peta

    }

    fun savePlacesFromCallback(placesCallback: List<Place>, context: Context) {
        val prefs = defaultPrefs(context)
        var places_ids = mutableSetOf<String>()

        for (place in placesCallback) {
            val id = place.id
            places_ids.add(id)
//            prefs[place.displayName] = id
            prefs[id.plus(Constants.places_display_name)] = place.displayName
            prefs[id.plus(Constants.places_latitude)] = place.location.latitude.toFloat()
            prefs[id.plus(Constants.places_longitude)] = place.location.longitude.toFloat()
            prefs[id.plus(Constants.places_address)] = place.address
            prefs[id.plus(Constants.places_main_playlist)] = place.mainPlaylist?.id
        }

        prefs.edit().putStringSet("places_ids", places_ids).apply()
    }

    fun logout(context: Context) {
        val prefs = defaultPrefs(context)
        prefs.edit().clear().apply()
    }
}