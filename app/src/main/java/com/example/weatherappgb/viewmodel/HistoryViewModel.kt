package com.example.weatherappgb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherappgb.model.Weather
import com.example.weatherappgb.model.detailsrepository.DetailsRepositoryAll
import com.example.weatherappgb.model.detailsrepository.RoomRepositoryImpl

class HistoryViewModel (private val liveData: MutableLiveData<AppState> = MutableLiveData(),
                        private val repository: RoomRepositoryImpl = RoomRepositoryImpl()
) :ViewModel() {
    fun getData(): LiveData<AppState> {
        return liveData
    }

    interface CallbackForAll {
        fun onResponse(listWeather: List<Weather>)
    }

    fun getAll() {
        repository.getAllWeatherDetails(object : CallbackForAll {
            override fun onResponse(listWeather: List<Weather>) {
                liveData.postValue(AppState.Success(listWeather))
            }

        })
    }
}