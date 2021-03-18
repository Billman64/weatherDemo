package com.twitter.challenge.model

import WeatherData
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.Url

interface FutureWeatherAPI {
    @GET
    fun getFutureWeather(@Url futureDay:String): retrofit2.Call<WeatherData>

    // future_<n>.json

//    Call<WeatherData> getFutureWeather(@Url day:Int)

//    @GET("future_\$.json")
//    fun getCurrentWeather(@Url("days") days:Int): retrofit2.Call<JsonObject>
}