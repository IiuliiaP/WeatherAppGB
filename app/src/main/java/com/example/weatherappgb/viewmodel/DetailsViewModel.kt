package com.example.weatherappgb.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherappgb.model.City
import com.example.weatherappgb.model.detailsrepository.DetailsRepository
import com.example.weatherappgb.model.detailsrepository.DetailsRepositoryRetrofitImpl
import com.example.weatherappgb.model.Weather
import com.example.weatherappgb.model.detailsrepository.DetailsRepositoryAdd
import com.example.weatherappgb.model.detailsrepository.RoomRepositoryImpl

class DetailsViewModel(private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
                       private val repository: DetailsRepository = DetailsRepositoryRetrofitImpl(),
                       private val repositoryAdd: DetailsRepositoryAdd = RoomRepositoryImpl()
) : ViewModel() {
    fun getLiveData() = liveData

    fun getWeather(city: City){
        repository.getWeatherDetails(city, object: Callback{
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
                repositoryAdd.addWeather(weather)
            }
        })
    }
    interface Callback{
        fun onResponse(weather: Weather)
    }

}