package com.example.weatherappgb.model.detailsrepository

import com.example.weatherappgb.viewmodel.HistoryViewModel

interface DetailsRepositoryAll {
    fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll)
}