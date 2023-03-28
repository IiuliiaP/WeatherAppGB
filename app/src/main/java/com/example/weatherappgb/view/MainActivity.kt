package com.example.weatherappgb.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.weatherappgb.R
import com.example.weatherappgb.view.history.HistoryListFragment

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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
     when(item.itemId){
         R.id.menu_history-> {
             supportFragmentManager.beginTransaction()
                 .add(R.id.fragment_container, HistoryListFragment.newInstance())
                 .addToBackStack("").commit()
         }
     }
        return super.onOptionsItemSelected(item)
    }

}


