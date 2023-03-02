package com.example.weatherappgb.model

class RepositoryImpl: Repository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(200L)
        return Weather()
    }

    override fun getWeatherFromLocal(): Weather {
        Thread.sleep(200L)
        return Weather()
    }
}