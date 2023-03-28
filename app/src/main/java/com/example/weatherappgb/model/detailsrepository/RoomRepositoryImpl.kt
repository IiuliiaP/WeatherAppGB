package com.example.weatherappgb.model.detailsrepository

import com.example.weatherappgb.App
import com.example.weatherappgb.domain.room.HistoryEntity
import com.example.weatherappgb.model.City
import com.example.weatherappgb.model.Weather
import com.example.weatherappgb.viewmodel.DetailsViewModel
import com.example.weatherappgb.viewmodel.HistoryViewModel

class RoomRepositoryImpl: DetailsRepositoryAll, DetailsRepository , DetailsRepositoryAdd{
    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) {
        Thread{
            callback.onResponse(convertHistoryEntityToWeather(App.getHistoryDao().getAll()))
        }.start()

    }

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        Thread{
            val list = convertHistoryEntityToWeather(App.getHistoryDao().getHistoryForCity(city.name))
            callback.onResponse(list.last())
        }.start()


    }
    override fun addWeather(weather: Weather) {
        Thread{
            App.getHistoryDao().insert(convertWeatherToEntity(weather))
        }.start()

    }
    fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>):List<Weather> {
        return entityList.map {
            Weather(City(it.city, 0.0, 0.0), it.temperature, it.feelsLike, it.icon) }
    }
    fun convertWeatherToEntity(weather: Weather): HistoryEntity {
        return HistoryEntity(0, weather.city.name, weather.temperature, weather.feelsLike, weather.icon)
    }

}