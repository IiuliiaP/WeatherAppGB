package com.example.weatherappgb.view.weatherdetails

import android.app.IntentService
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weatherappgb.*
import com.example.weatherappgb.model.dto.WeatherDTO
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailsService(val name: String = ""): IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = it.getDoubleExtra(KEY_LAT_EXTRA,0.0)
            val lon = it.getDoubleExtra(KEY_LON_EXTRA,0.0)

            val urlText = "https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon"
            val uri = URL(urlText)
            val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection).apply {
                    connectTimeout = 1000
                    addRequestProperty(KEY_REQUEST_API_KEY, YANDEX_KEY_VALUE)
                }

            try {
                val headers = urlConnection.headerFields
                val responseCode = urlConnection.responseCode
                val responseMessage = urlConnection.responseMessage
                val serverside = 500..599
                val clientside = 400..499
                val responseOk = 200..299
                when (responseCode) {
                    in serverside -> {
                        //TODO
                    }
                    in clientside -> {
                        //TODO
                    }
                    in responseOk -> {
                        val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                        val weatherDTO: WeatherDTO = Gson().fromJson(buffer, WeatherDTO::class.java)
                        val message = Intent(KEY_WAVE_SERVICE_BROADCAST)
                        message.putExtra(
                            KEY_BROADCAST_WEATHER, weatherDTO
                        )
                        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
                    }
                    else -> {}
                }


            }catch (e: JsonSyntaxException){

            } finally {
                urlConnection.disconnect()
            }

        }
    }
}