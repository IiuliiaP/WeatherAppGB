package com.example.weatherappgb.view.weatherdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.bumptech.glide.Glide
import com.example.weatherappgb.*
import com.example.weatherappgb.databinding.FragmentWeatherDetailsBinding
import com.example.weatherappgb.model.*
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.weatherappgb.viewmodel.DetailsState
import com.example.weatherappgb.viewmodel.DetailsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.annotations.SerializedName

class WeatherDetailsFragment : Fragment() {

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
    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
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
        viewModel.getLiveData().observe(viewLifecycleOwner
        ) { t -> renderData(t) }
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentWeather = it
            viewModel.getWeather(currentWeather.city)

        }
    }
    private fun renderData(detailsState: DetailsState){
        when(detailsState){
            is DetailsState.Error -> {
                Snackbar.make(binding.root, "ошибка", Snackbar.LENGTH_SHORT).show()
            }
            DetailsState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE }
            is DetailsState.Success -> {
                val weather = detailsState.weather
                with(binding){
                    loadingLayout.visibility = View.GONE
                    cityName.text = currentWeather.city.name
                    coordinates.text = String.format(
                        getString(R.string.city_coordinates), currentWeather.city.lat.toString()
                        ,currentWeather.city.lon.toString()
                    )
                    temperature.text = weather.temperature.toString()
                    feelsLike.text = weather.feelsLike.toString()
                    Glide.with(requireContext())
                       .load("https://freepngimg.com/thumb/house/84949-house-housing-recreation-city-hd-image-free-png.png")
                       .into(headerIcon)
                    weatherIcon.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")
                }
            }
        }
    }

    fun ImageView.loadSvg(url:String){
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()
        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }


}
