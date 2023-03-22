package com.example.weatherappgb.view.weatherdetails

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weatherappgb.*
import com.example.weatherappgb.databinding.FragmentWeatherDetailsBinding
import com.example.weatherappgb.model.*
import com.example.weatherappgb.model.dto.WeatherDTO

class WeatherDetailsFragment : Fragment(), OnServerResponse {

    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding: FragmentWeatherDetailsBinding
    get(){
        return _binding!!
    }

    companion object {
        const val KEY_BUNDLE_WEATHER = "weather"
        fun newInstance(bundle: Bundle): WeatherDetailsFragment {
            val fragment = WeatherDetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
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
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter( KEY_WAVE_SERVICE_BROADCAST))

        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentWeather = it
            //WeatherLoader(this@WeatherDetailsFragment).loadWeather(it.city.lon, it.city.lat)
            requireActivity().startService(Intent(requireContext(), DetailsService::class.java).apply {
                putExtra(KEY_LAT_EXTRA, it.city.lat)
                putExtra(KEY_LON_EXTRA, it.city.lon)
            })
        }
    }
    private fun renderData(weatherDTO: WeatherDTO){
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
    private val receiver = object: BroadcastReceiver(){
        override fun onReceive(contex: Context?, intent: Intent?) {
            intent?.let {intent ->
                 intent.getParcelableExtra<WeatherDTO>(KEY_BROADCAST_WEATHER)?.let {
                     onResponse(it)
                 }
            }
        }

    }
}
