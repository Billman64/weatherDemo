package com.twitter.challenge.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Headers

interface CurrentWeatherAPI {
//    @Headers(value = [""])
    @GET("current.json")
    fun getCurrentWeather(): retrofit2.Call<JsonObject>
}