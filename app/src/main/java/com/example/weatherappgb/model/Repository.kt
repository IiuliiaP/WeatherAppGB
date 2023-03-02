package com.example.weatherappgb.model

interface Repository {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocal(): Weather
}