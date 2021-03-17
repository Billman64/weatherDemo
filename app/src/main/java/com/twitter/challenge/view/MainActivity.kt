package com.twitter.challenge.view

import CurrentWeather
import kotlinx.coroutines.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.twitter.challenge.R
import com.twitter.challenge.TemperatureConverter
import com.twitter.challenge.model.CurrentWeatherAPI
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    val TAG = this.javaClass.simpleName + "-demo---"

    val currentTemp = "0"
    val currentWind = "0"
    val currentCloudiness = "0"

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val temperatureView: TextView = findViewById(R.id.temperature) as TextView
        temperatureView.setText(getString(R.string.temperature, 34f, TemperatureConverter.celsiusToFahrenheit(34f)))

        getWeatherData()
    }

    override fun onSaveInstanceState(outState:Bundle){
        super.onSaveInstanceState(outState)

        // orientation change - put downloaded weather data into bundle
        outState.putString("currentTemp", currentTemp)
        outState.putString("currentWind", currentWind)
        outState.putString("currentCloudiness", currentCloudiness)


    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)

        // orientation change - restore downloaded weather data
        savedInstanceState?.let{

            var currentTemp = savedInstanceState.getString("currentTemp")
            var currentWind = savedInstanceState.getString("currentWind")
            var currentCloudiness = savedInstanceState.getString("currentCloudiness")


        }

    }

    private fun getWeatherData(){

        Log.d(TAG, "getWeatherData()")

        // Retrofit builder
        val currentWeatherApi = Retrofit.Builder()
                .baseUrl("https://twitter-code-challenge.s3.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CurrentWeatherAPI::class.java)

        // Coroutine for network call
        GlobalScope.launch(Dispatchers.IO){
            Log.d(TAG, "Coroutine for network call")

            try{
                Log.d(TAG, "getting weather...")
                val responseCurrentWeather = currentWeatherApi.getCurrentWeather().awaitResponse()
                Log.d(TAG, "response: ${responseCurrentWeather.code()} |  ${responseCurrentWeather.message()}")
                Log.d(TAG, " response body: ${responseCurrentWeather.body()}")
                val data = responseCurrentWeather.body()!!.getAsJsonObject()
                Log.d(TAG, " data length: ${data.toString().substring(0,50)}")

                // update UI
                withContext(Dispatchers.Main){
                    var tempJson = data.asJsonObject.getAsJsonObject("weather")
                    var temp = tempJson.getAsJsonPrimitive("temp").toString()

                    temperature.text = temp


                }

            } catch (e:java.lang.Exception){
                Log.e(TAG, " network error: ${e.message}")


            }



        }
    }


}