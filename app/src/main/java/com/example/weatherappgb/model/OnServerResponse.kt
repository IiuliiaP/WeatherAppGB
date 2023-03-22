package com.example.weatherappgb.model

import com.example.weatherappgb.model.dto.WeatherDTO

fun interface OnServerResponse {
    fun onResponse(weatherDTO: WeatherDTO)
}