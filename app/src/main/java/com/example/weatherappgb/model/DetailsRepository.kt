package com.example.weatherappgb.model

import com.example.weatherappgb.viewmodel.DetailsViewModel


interface DetailsRepository {
    fun getWeatherDetails(city: City,callback: DetailsViewModel.Callback)
}