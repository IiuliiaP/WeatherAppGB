package com.example.weatherappgb.model

class RepositoryImpl: Repository {
    override fun getWeatherFromServer(): Weather {
        Thread.sleep(200L)
        return Weather()
    }
    override fun getWorldWeatherFromLocal(): List<Weather> {
        return getWorldCities()
    }
    override fun getRusWeatherFromLocal(): List<Weather>  {
        return getRussianCities()
    }


}