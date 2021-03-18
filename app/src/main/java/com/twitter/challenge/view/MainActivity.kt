package com.twitter.challenge.view

import kotlinx.coroutines.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.twitter.challenge.R
import com.twitter.challenge.utilities.TemperatureConverter.celsiusToFahrenheit
import com.twitter.challenge.model.CurrentWeatherAPI
import com.twitter.challenge.model.FutureWeatherAPI
import com.twitter.challenge.utilities.Average
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    val TAG = this.javaClass.simpleName + "-demo---"

    var currentTemp = "0"
    var currentWind = "0"
    var currentCloudiness = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(savedInstanceState==null) getWeatherData()

        button_get_next_5_days.setOnClickListener {
            getFutureWeather(5)
        }


        //        val temperatureView: TextView = findViewById(R.id.temperatureC) as TextView   // default from starter code
        //        temperatureView.setText(getString(R.string.temperature, 34f, TemperatureConverter.celsiusToFahrenheit(34f)))
    }

    override fun onSaveInstanceState(outState:Bundle){
        super.onSaveInstanceState(outState)

        // orientation change - put downloaded weather data into bundle
        outState.putString("currentTemp", currentTemp)
        outState.putString("currentWind", currentWind)
        outState.putString("currentCloudiness", currentCloudiness)
        outState.putString("standardDeviation", stdDevNext5Days.text.toString())
        Log.d(TAG, "onSaveInstanceState() -  temp: ${outState.get("currentTemp")} wind: ${outState.get("currentWind")} cloudiness: ${outState.get("currentCloudiness")}")


    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle){
        super.onRestoreInstanceState(savedInstanceState)

        // orientation change - restore downloaded weather data

        currentTemp = savedInstanceState.getString("currentTemp")!!
        currentWind = savedInstanceState.getString("currentWind")!!
        currentCloudiness = savedInstanceState.getString("currentCloudiness")!!
        val stdDev = savedInstanceState.getString("standardDeviation")

        Log.d(TAG, "onRestoreInstanceState() - temp: ${savedInstanceState.get("currentTemp")} wind: ${savedInstanceState.get("currentWind")} cloudiness: ${savedInstanceState.get("currentCloudiness")}")

        // update UI
        currentTemp.let{
            temperatureC.text = currentTemp + " C"
            temperatureF.text = celsiusToFahrenheit(currentTemp.toFloat()).toString() + " F"
        }
        currentWind.let{ speed.text = currentWind }
        currentCloudiness.let{
            Log.d(TAG, " cloudiness: ${currentCloudiness}")
            if(currentCloudiness.toFloat()>50f) weatherSymbol.visibility = View.VISIBLE }
        stdDev.let{ stdDevNext5Days.text = stdDev}

    }

    private fun getWeatherData(){

        //TODO: refactor data gathering into a modelView
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
                // get data
                Log.d(TAG, "getting weather...")
                val responseCurrentWeather = currentWeatherApi.getCurrentWeather().awaitResponse()
                Log.d(TAG, "response: ${responseCurrentWeather.code()} |  ${responseCurrentWeather.message()}")
                Log.d(TAG, " response body: ${responseCurrentWeather.body()}")
                val data = responseCurrentWeather.body()!!.getAsJsonObject()
                Log.d(TAG, " data length: ${data.toString().substring(0,50)}")

                // parse data
                val tempJson = data.asJsonObject.getAsJsonObject("weather")
                currentTemp = tempJson.getAsJsonPrimitive("temp").toString()

                val windJson = data.getAsJsonObject("wind")
                currentWind = windJson.getAsJsonPrimitive("speed").toString()

                val cloudsJson = data.getAsJsonObject("clouds")
                currentCloudiness = cloudsJson.getAsJsonPrimitive("cloudiness").toString()


                // update UI
                withContext(Dispatchers.Main){

                    temperatureC.text = currentTemp + " C"
                    temperatureF.text = celsiusToFahrenheit(currentTemp.toFloat()).toString() + " F"

                    speed.text = currentWind

                    if(currentCloudiness.toFloat()>50f) weatherSymbol.visibility = View.VISIBLE
                }

            } catch (e:Exception){
                Log.e(TAG, " network error: ${e.message}")

            }

        }
    }

    private fun getFutureWeather(){
        getFutureWeather(5) // default to n=5 days
    }

    private fun getFutureWeather(numDays: Int){

        Log.d(TAG, "getFutureWeather($numDays)")

        // Retrofit builder
        val currentWeatherApi = Retrofit.Builder()
                .baseUrl("https://twitter-code-challenge.s3.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FutureWeatherAPI::class.java)


        GlobalScope.launch(Dispatchers.IO){

            val fileNamePrepend = "future_"
            val fileNameAppend = ".json"
            var stdDev = 0f
            var avg = 0f
            var tempOfDay = 0f
            val dayArray:ArrayList<Float> = arrayListOf()

            try {

                // Get data - loop through each day
                for(day in 1..numDays) {
                    val responseDay = currentWeatherApi.getFutureWeather(fileNamePrepend + day + fileNameAppend).awaitResponse()
                    Log.v(TAG, " response received. code: ${responseDay.code()} message: ${responseDay.message()}")
                    tempOfDay = responseDay.body()!!.weather.temp.toFloat()
                    dayArray.add(tempOfDay)
                    Log.d(TAG, "tempOfDay: $tempOfDay")
                }

                // calculate standard deviation for next n days
                avg = Average.avg(dayArray)
                Log.v(TAG, "avg: $avg")

                for(day in 0..numDays-1){
                    stdDev += (dayArray.get(day) - avg).pow(2)
                }
                stdDev /= (numDays - 1)
                stdDev = sqrt(stdDev)
                Log.d(TAG, "stdDev: $stdDev")


                // update UI
                withContext(Dispatchers.Main){
                    stdDevNext5Days.text = stdDev.toString()
                }
            } catch(e:Exception){
                Log.e(TAG, " network error: ${e.message}")
            }
        }
    }
}