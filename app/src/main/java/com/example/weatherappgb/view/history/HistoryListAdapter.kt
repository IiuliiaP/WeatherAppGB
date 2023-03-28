package com.example.weatherappgb.view.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherappgb.databinding.HistoryListItemBinding
import com.example.weatherappgb.model.Weather

class HistoryListAdapter(private var data: List<Weather> = listOf())
    : RecyclerView.Adapter<HistoryListAdapter.CityHolder> (){

    fun setData(dataNew: List<Weather>) {
        this.data = dataNew
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryListAdapter.CityHolder {
        val binding = HistoryListItemBinding
            .inflate(LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CityHolder(binding.root)
    }

    override fun onBindViewHolder(holder: HistoryListAdapter.CityHolder, position: Int) {
        holder.bind(data.get(position))
    }
    override fun getItemCount() = data.size

    inner class CityHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(weather: Weather){
            HistoryListItemBinding.bind(itemView).apply {
                tvCityName.text = weather.city.name
                tvTemperature.text = weather.temperature.toString()
                tvFeelsLike.text = weather.feelsLike.toString()

            }
        }
    }


}