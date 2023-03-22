package com.example.weatherappgb.model

import android.os.Handler
import android.os.Looper
import com.example.weatherappgb.KEY_REQUEST_API_KEY
import com.example.weatherappgb.YANDEX_KEY_VALUE
import com.example.weatherappgb.model.dto.WeatherDTO
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
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
                addRequestProperty(KEY_REQUEST_API_KEY, YANDEX_KEY_VALUE)
            }
            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage

                val serverside = 500..599
                val clientside = 400..499
                val responseOk = 200..299
                when(responseCode) {
                    in serverside -> {
                        //TODO
                    }
                    in clientside -> {
                        //TODO
                    }
                    in responseOk -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        Handler(Looper.getMainLooper()).post { onServerResponseListener.onResponse(weatherDTO) }
                    }
                }


            } catch (e: JsonSyntaxException){}
            finally {
                urlConnection.disconnect()
            }

        }.start()
    }

}