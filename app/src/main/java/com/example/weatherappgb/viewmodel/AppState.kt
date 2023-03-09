package com.example.weatherappgb.viewmodel

import com.example.weatherappgb.model.Weather

sealed class AppState {
    object Loading: AppState()
    data class Success(val weatherData: List<Weather>): AppState()
    data class Error(val error: Throwable): AppState()
}