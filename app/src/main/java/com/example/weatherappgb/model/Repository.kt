package com.example.weatherappgb.model

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWorldWeatherFromLocal(): List<Weather>
    fun getRusWeatherFromLocal(): List<Weather>
}