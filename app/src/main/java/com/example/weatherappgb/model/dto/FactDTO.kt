package com.example.weatherappgb.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FactDTO(
    val condition: String,
    val feels_like: Int?,
    val icon: String,
    val temp: Int?
    ):Parcelable