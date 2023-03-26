package com.example.weatherappgb.domain.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val city: String,
    val temperature: Int,
    val feelsLike: Int,
    val icon: String
)
