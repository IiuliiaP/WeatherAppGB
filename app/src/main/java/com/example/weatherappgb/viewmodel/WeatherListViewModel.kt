package com.example.weatherappgb.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherappgb.model.RepositoryImpl

class WeatherListViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData(),
                           private val repository: RepositoryImpl = RepositoryImpl())
    : ViewModel() {
        fun getData(): LiveData<AppState>{
            return liveData
        }
    fun getWeatherRus() = getWeather(true)
    fun getWeatherWorld() = getWeather(false)
    private fun getWeather(isRus: Boolean){
        Thread{
            liveData.postValue((AppState.Loading))
            if((0..10).random()> 5){
                val answer = if(isRus) repository.getRusWeatherFromLocal()else
                    repository.getWorldWeatherFromLocal()
                liveData.postValue(AppState.Success(answer))}
            else{
                liveData.postValue(AppState.Error(IllegalAccessError("ошибка")))
            }
        }.start()
    }

    }