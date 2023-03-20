package com.example.weatherappgb.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherappgb.R
import com.example.weatherappgb.databinding.FragmentWeatherDetailsBinding
import com.example.weatherappgb.model.*

class WeatherDetailsFragment : Fragment(), OnServerResponse {

    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding: FragmentWeatherDetailsBinding
    get(){
        return _binding!!
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"
        fun newInstance(bundle: Bundle): WeatherDetailsFragment{
            val fragment = WeatherDetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherDetailsBinding.inflate(inflater, container,false)
        return binding.root
    }
    lateinit var currentWeather: Weather
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(BUNDLE_EXTRA)?.let {
            currentWeather = it
            WeatherLoader(this@WeatherDetailsFragment)
                .loadWeather(it.city.lon, it.city.lat)
        }
    }
    private fun renderData(weatherDTO:WeatherDTO){
        with(binding){
            cityName.text = currentWeather.city.name
            coordinates.text = String.format(
                getString(R.string.city_coordinates), currentWeather.city.lat.toString()
                ,currentWeather.city.lon.toString()
            )
            temperature.text = weatherDTO.factDTO?.temp.toString()
            feelsLike.text = weatherDTO.factDTO?.feels_like.toString()
        }
    }
    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }
}
