package com.kuliah.greenhouse_iot.data.model.weather

data class WeatherData(
	val cod: String,
	val message: Int,
	val cnt: Int,
	val list: List<WeatherItem>,
	val city: City,
)

data class WeatherItem(
	val dt: Long,
	val main: MainData,
	val weather: List<WeatherDescription>,
	val clouds: Clouds,
	val wind: Wind,
	val visibility: Int,
	val pop: Double,
	val rain: Rain?,
	val sys: Sys,
	val dt_txt: String
)

data class MainData(
	val temp: Double,
	val feels_like: Double,
	val temp_min: Double,
	val temp_max: Double,
	val pressure: Int,
	val humidity: Int,
	val seaLevel: Int,
	val grndLevel: Int,
)

data class WeatherDescription(
	val main: String,
	val description: String,
	val icon: String
)

data class Clouds(
	val all: Int
)

data class Wind(
	val speed: Double,
	val deg: Int,
	val gust: Double,
)

data class Rain(
	val `3h`: Double?
)

data class Sys(
	val pod: String
)

data class City(
	val id: Long,
	val name: String,
	val coord: Coord,
	val country: String,
	val population: Long,
	val timezone: Long,
	val sunrise: Long,
	val sunset: Long,
)

data class Coord(
	val lat: Double,
	val lon: Double,
)

