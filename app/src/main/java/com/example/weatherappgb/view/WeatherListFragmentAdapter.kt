package com.example.weatherappgb.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappgb.R
import com.example.weatherappgb.model.Weather

class WeatherListFragmentAdapter:
    RecyclerView.Adapter<WeatherListFragmentAdapter.WeatherListViewHolder>() {
    private var weatherData: List<Weather> = listOf()
    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    inner class WeatherListViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(weather: Weather){
            itemView.findViewById<TextView>(R.id.weather_list_item).text = weather.city.name
            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    weather.city.name,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherListViewHolder {
        return WeatherListViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_list_item, parent, false) as View)
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    override fun onBindViewHolder(holder: WeatherListViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }
}
