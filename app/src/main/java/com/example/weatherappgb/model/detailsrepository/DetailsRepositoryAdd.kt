package com.example.weatherappgb.model.detailsrepository

import com.example.weatherappgb.model.Weather

interface DetailsRepositoryAdd {
    fun addWeather(weather: Weather)
}