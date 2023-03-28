package com.example.weatherappgb.model.detailsrepository

import com.example.weatherappgb.model.City
import com.example.weatherappgb.viewmodel.DetailsViewModel


interface DetailsRepository {
    fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}