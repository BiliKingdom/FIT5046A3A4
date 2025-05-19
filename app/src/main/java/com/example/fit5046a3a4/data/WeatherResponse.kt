package com.example.fit5046a3a4.data

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val name: String,
    val sys: Sys,
    val weather: List<WeatherDescription>,
    val main: WeatherMain
)

@Serializable
data class Sys(val country: String)

@Serializable
data class WeatherDescription(val main: String, val description: String, val icon: String)

@Serializable
data class WeatherMain(val temp: Float)
