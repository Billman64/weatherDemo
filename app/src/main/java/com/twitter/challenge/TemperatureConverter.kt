package com.twitter.challenge

object TemperatureConverter {
    /**
     * Converts temperature in Celsius to temperature in Fahrenheit.
     *
     * @param temperatureInCelsius Temperature in Celsius to convert.
     * @return Temperature in Fahrenheit.
     */
    fun celsiusToFahrenheit(temperatureInCelsius: Float): Float {
        return temperatureInCelsius * 1.8f + 32
    }
}