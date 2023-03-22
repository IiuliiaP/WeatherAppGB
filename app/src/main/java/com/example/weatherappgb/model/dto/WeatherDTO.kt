package com.example.weatherappgb.model.dto

import android.os.Parcelable
import com.example.weatherappgb.model.dto.FactDTO
import com.example.weatherappgb.model.dto.InfoDTO
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherDTO(
    @SerializedName("fact")
    val factDTO: FactDTO?,
    @SerializedName("info")
    val infoDTO: InfoDTO?

):Parcelable