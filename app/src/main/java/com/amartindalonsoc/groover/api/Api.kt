package com.amartindalonsoc.groover.api

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import com.amartindalonsoc.groover.R
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Api {
    lateinit var retrofit_spoti_callback: Retrofit.Builder

    fun Api() {
    }

    //TODO Pasar las baseUrl al archivo de Strings

    fun azureApiRequest(): Endpoints {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).readTimeout(120, TimeUnit.SECONDS)
        return Retrofit.Builder()
            .baseUrl("https://tfggroover.azurewebsites.net/")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build().create(Endpoints::class.java)
    }

    fun spotifyApiRequest(): Endpoints {
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()//.addInterceptor(interceptor)
        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build().create(Endpoints::class.java)
    }
}