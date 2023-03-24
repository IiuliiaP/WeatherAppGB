package com.example.weatherappgb

import com.example.weatherappgb.model.Weather
import com.example.weatherappgb.model.dto.FactDTO
import com.example.weatherappgb.model.dto.WeatherDTO
import com.example.weatherappgb.model.getDefaultCity

class Utils {
}
const val KEY_REQUEST_API_KEY = "X-Yandex-API-Key"
const val YANDEX_KEY_VALUE = "d9686252-f05e-47fc-a78c-ef84a32d3d0b"
const val KEY_LAT_EXTRA = "Lat"
const val KEY_LON_EXTRA = "Lon"
const val KEY_BROADCAST_WEATHER = "weather_broadcast"
const val KEY_WAVE_SERVICE_BROADCAST = "myaction_way"

const val YANDEX_DOMAIN = "https://api.weather.yandex.ru/"
const val YANDEX_ENDPOINT = "v2/informers?"
fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO? = weatherDTO.factDTO
    return (Weather(getDefaultCity(), fact!!.temp!!, fact.feels_like!!,fact.icon))
}