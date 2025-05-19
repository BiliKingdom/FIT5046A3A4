package com.example.fit5046a3a4.data.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import com.example.fit5046a3a4.data.WeatherResponse

suspend fun fetchWeather(lat: Double, lon: Double): WeatherResponse {
    val apiKey = "dfc94cad127ce5c07281b96adb4b043e"
    val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&units=metric"

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    return client.get(url).body()
}

