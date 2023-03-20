package com.example.weatherappgb.model

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}