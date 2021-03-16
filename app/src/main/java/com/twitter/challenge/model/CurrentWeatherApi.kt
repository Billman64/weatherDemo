data class CurrentWeatherApi(
    val clouds: Clouds,
    val coord: Coord,
    val name: String,
    val rain: Rain,
    val weather: Weather,
    val wind: Wind
)