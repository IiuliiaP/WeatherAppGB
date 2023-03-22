package com.example.weatherappgb.model

import android.os.Handler
import android.os.Looper
import com.example.weatherappgb.model.dto.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection



class WeatherLoader(private val onServerResponseListener: OnServerResponse) {

    fun loadWeather(lat: Double, lon: Double)  {
        Thread{
            val urlText = "https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}"
            val uri = URL(urlText)

            val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection).apply {
                readTimeout = 2000
                addRequestProperty("X-Yandex-API-Key", "d9686252-f05e-47fc-a78c-ef84a32d3d0b")
            }
            val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val weatherDTO: WeatherDTO =  Gson().fromJson(getLines(buffer), WeatherDTO::class.java)
            Handler(Looper.getMainLooper()).post { onServerResponseListener.onResponse(weatherDTO) }

        }.start()
    }
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n")) }


}