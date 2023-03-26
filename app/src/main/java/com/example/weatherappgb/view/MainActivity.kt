package com.example.weatherappgb.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherappgb.R

import com.example.weatherappgb.view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                WeatherListFragment.newInstance())
                .commit()
        }
    }
}


