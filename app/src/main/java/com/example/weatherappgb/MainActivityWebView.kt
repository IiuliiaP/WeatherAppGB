package com.example.weatherappgb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherappgb.databinding.ActivityMainWebViewBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivityWebView : AppCompatActivity() {
    lateinit var binding: ActivityMainWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ok.setOnClickListener {
            val urlText = binding.url.text.toString()
            val uri = URL(urlText)
            val urlConnection: HttpsURLConnection = (uri.openConnection() as HttpsURLConnection).apply {
                readTimeout = 2000
            }
            Thread{
                val buffer = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val result = getLinesTogether(buffer)
                runOnUiThread {
                    binding.webview.loadDataWithBaseURL(null,result, "text/html; utf-8", "utf-8",null)
                }
            }.start()
        }
    }
    private fun getLinesTogether(bufferedReader: BufferedReader): String{
        return bufferedReader.lines().collect(Collectors.joining("\n"))
    }
}