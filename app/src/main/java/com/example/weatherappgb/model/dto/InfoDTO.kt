package com.example.weatherappgb.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InfoDTO(
    val lat: Double?,
    val lon: Double?
    ):Parcelable