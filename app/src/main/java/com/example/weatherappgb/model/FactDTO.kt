package com.example.weatherappgb.model

data class FactDTO(
    val condition: String,
    val feels_like: Int?,
    val icon: String?,
    val temp: Int?
    )