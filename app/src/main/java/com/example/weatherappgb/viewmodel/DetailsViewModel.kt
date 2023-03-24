package com.example.weatherappgb.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherappgb.model.City
import com.example.weatherappgb.model.DetailsRepository
import com.example.weatherappgb.model.DetailsRepositoryRetrofitImpl
import com.example.weatherappgb.model.Weather

class DetailsViewModel(private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
                       private val repository: DetailsRepository =DetailsRepositoryRetrofitImpl ()
) : ViewModel() {
    fun getLiveData() = liveData

    fun getWeather(city: City){
        repository.getWeatherDetails(city, object: Callback{
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
            }
        })
    }
    interface Callback{
        fun onResponse(weather: Weather)
    }

}