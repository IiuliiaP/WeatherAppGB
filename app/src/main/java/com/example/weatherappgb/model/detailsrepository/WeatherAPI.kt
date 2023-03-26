package com.example.weatherappgb.model.detailsrepository

import com.example.weatherappgb.KEY_LAT_EXTRA
import com.example.weatherappgb.KEY_LON_EXTRA
import com.example.weatherappgb.KEY_REQUEST_API_KEY
import com.example.weatherappgb.YANDEX_ENDPOINT
import com.example.weatherappgb.model.dto.WeatherDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {
    @GET(YANDEX_ENDPOINT)
    fun getWeather(
        @Header(KEY_REQUEST_API_KEY) apikey: String,
        @Query(KEY_LAT_EXTRA) lat: Double,
        @Query(KEY_LON_EXTRA) lon: Double
    ): Call<WeatherDTO>
}