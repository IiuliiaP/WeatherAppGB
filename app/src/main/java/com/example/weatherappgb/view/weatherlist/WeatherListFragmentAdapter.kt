package com.example.weatherappgb.view.weatherlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappgb.R
import com.example.weatherappgb.model.Weather

class WeatherListFragmentAdapter(private var onItemViewClickListener: OnItemViewClickListener?):
    RecyclerView.Adapter<WeatherListFragmentAdapter.WeatherListViewHolder>() {
    private var weatherData: List<Weather> = listOf()
    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    inner class WeatherListViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(weather: Weather) {
            itemView.findViewById<TextView>(R.id.weather_list_item_text_view).text =
                weather.city.name
            itemView.setOnClickListener {
                onItemViewClickListener?.onItemViewClick(weather)
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
    fun removeListener() {
        onItemViewClickListener = null
    }


}
