package com.example.weatherappgb.model

import com.example.weatherappgb.YANDEX_DOMAIN
import com.example.weatherappgb.YANDEX_KEY_VALUE
import com.example.weatherappgb.convertDtoToModel
import com.example.weatherappgb.model.dto.WeatherDTO
import com.example.weatherappgb.viewmodel.DetailsViewModel
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsRepositoryRetrofitImpl: DetailsRepository {
    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(YANDEX_DOMAIN)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)//создание ретрофита + запрос

        weatherAPI.getWeather(YANDEX_KEY_VALUE, city.lat, city.lon).enqueue(object : Callback<WeatherDTO>{
            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        val weather = convertDtoToModel(it)
                        weather.city = city
                        callback.onResponse(weather)
                    }
                }
            }

            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


    }
}