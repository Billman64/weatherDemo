package com.twitter.challenge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    val currentTemp = "0"
    val currentWind = "0"
    val currentCloudiness = "0"
    
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val temperatureView: TextView = findViewById(R.id.temperature) as TextView
        temperatureView.setText(getString(R.string.temperature, 34f, TemperatureConverter.celsiusToFahrenheit(34)))
    }

    @Override
    onSaveInstanceState(outState:Bundle){
        super.onSaveInstanceState(outstate)

        // orientation change - put downloaded weather data into bundle
        outState.putString("currentTemp", currentTemp)
        outState.putString("currentWind", currentWind)
        outState.putString("currentCloudiness", currentCloudiness)


    }

    @Override
    onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)

        // orientation change - restore downloaded weather data
        savedInstanceState?.let{

            currentTemp = savedInstanceState.getString("currentTemp")
            currentWind = savedInstanceState.getString("currentWind")
            currentCloudiness = savedInstanceState.getString("currentCloudiness")


        }

    }

    fun getWeatherData(){

        // Retrofit builder
        val currentWeatherApi = Retrofit.Builder()
                .baseUrl("https://twitter-code-challenge.s3.amazonaws.com/current.json")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CurrentWeatherApi::class.java)

        // Coroutine for network call
        GlobalScope.launch(Dispatchers.IO){

            try{


                // update UI
                withContext(Dispatchers.Main){


                }

            } catch (e:java.lang.Exception){


            }



        }
    }


}