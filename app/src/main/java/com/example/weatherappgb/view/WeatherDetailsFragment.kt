package com.example.weatherappgb.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappgb.databinding.FragmentWeatherDetailsBinding
import com.example.weatherappgb.viewmodel.AppState
import com.example.weatherappgb.viewmodel.WeatherDetailsViewModel

class WeatherDetailsFragment : Fragment() {
    private lateinit var viewModel: WeatherDetailsViewModel
    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding: FragmentWeatherDetailsBinding
    get(){
        return _binding!!
    }

    companion object {
        fun newInstance() = WeatherDetailsFragment()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherDetailsBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this).get(WeatherDetailsViewModel::class.java)
        val observer = Observer<AppState> { renderData(it)}

        viewModel.getData().observe(viewLifecycleOwner,observer)
        viewModel.getWeather()
    }
    fun renderData(data: AppState){
        when(data){
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                binding.message.text = "error!"
            }
            AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                binding.message.visibility = View.GONE
                binding.cityName.text = data.weatherData.city.name.toString()
                binding.temperature.text = data.weatherData.temperature.toString()
                Toast.makeText(requireContext(),"успешная загрузка", Toast.LENGTH_LONG).show()
            }
        }
    }
}