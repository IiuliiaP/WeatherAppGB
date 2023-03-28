package com.example.weatherappgb

import android.app.Application
import androidx.room.Room
import com.example.weatherappgb.domain.room.DataBase
import com.example.weatherappgb.domain.room.HistoryDao

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        private var db: DataBase? = null
        private var appContext: App? = null

        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                if (appContext != null) {
                    db = Room.databaseBuilder(appContext!!, DataBase::class.java, "db_name").allowMainThreadQueries()
                            .build()

                    } else throw IllegalStateException("Application is null while creating DataBase")
                }
            return db!!.historyDao()

        }
    }
}