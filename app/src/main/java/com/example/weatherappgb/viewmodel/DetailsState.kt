package com.example.weatherappgb.viewmodel

import com.example.weatherappgb.model.Weather

sealed class DetailsState{
    object Loading:DetailsState()
    data class Success(val weather: Weather):DetailsState()
    data class Error(val error:Throwable):DetailsState()
}
