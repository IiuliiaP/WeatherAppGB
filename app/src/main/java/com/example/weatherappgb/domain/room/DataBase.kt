package com.example.weatherappgb.domain.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(HistoryEntity::class), version = 1, exportSchema = false)
abstract class DataBase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}