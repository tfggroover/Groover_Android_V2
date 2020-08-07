package com.amartindalonsoc.groover.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.amartindalonsoc.groover.responses.SpotifyCallback

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

    fun saveUserFromCallback(spotifyCallback: SpotifyCallback, context: Context) {
        val prefs = defaultPrefs(context)
        prefs[Constants.spotify_user_token] = spotifyCallback.spotify.accessToken
        prefs[Constants.firebase_token] = spotifyCallback.firebase
        prefs[Constants.user_name] = spotifyCallback.spotifyUserData.displayName
        prefs[Constants.user_email] = spotifyCallback.spotifyUserData.email
        prefs[Constants.user_country] = spotifyCallback.spotifyUserData.country
        prefs[Constants.spotify_account_type] = spotifyCallback.spotifyUserData.product

//        saveString("firebase_token", spotifyCallback.firebase)
//        saveString("user_name", spotifyCallback.spotifyUserData.displayName)
//        saveString("user_email", spotifyCallback.spotifyUserData.email)
//        saveString("user_country", spotifyCallback.spotifyUserData.country)
//        saveString("spotify_account_type", spotifyCallback.spotifyUserData.product)
    }
}