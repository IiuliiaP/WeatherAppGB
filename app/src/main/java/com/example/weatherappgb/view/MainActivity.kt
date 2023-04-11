package com.example.weatherappgb.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
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
             navigate(HistoryListFragment.newInstance())
         }
         R.id.menu_content_provider-> {
           navigate(ContentProviderFragment.newInstance())
         }
         R.id.menu_yandex_map-> {
            navigate(YandexMapFragment.newInstance())
         }
     }

        return super.onOptionsItemSelected(item)
    }
    private fun navigate(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment)
            .addToBackStack("").commit()
    }

}


