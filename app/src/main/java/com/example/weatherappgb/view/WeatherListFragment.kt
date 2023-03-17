package com.example.weatherappgb.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappgb.R

import com.example.weatherappgb.databinding.FragmentWeatherListBinding
import com.example.weatherappgb.model.Weather
import com.example.weatherappgb.viewmodel.AppState
import com.example.weatherappgb.viewmodel.WeatherListViewModel
import com.google.android.material.snackbar.Snackbar


class WeatherListFragment : Fragment() {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get(){
            return _binding!!
        }

    private val adapter = WeatherListFragmentAdapter(object: OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(WeatherDetailsFragment.BUNDLE_EXTRA, weather)
                manager.beginTransaction()
                    .replace(R.id.fragment_container, WeatherDetailsFragment.newInstance(bundle))
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
            }
        })
    private var isDataSetRus: Boolean = true
    val viewModel: WeatherListViewModel by lazy {
        ViewModelProvider(this).get(WeatherListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = WeatherListFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleView.adapter = adapter
        binding.floatActionButton.setOnClickListener {changeWeatherDataSet()}
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            renderData(it) })
        viewModel.getWeatherRus()


    }
    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherWorld()
            binding.floatActionButton.setImageResource(R.drawable.planet_ic)
        } else {
            viewModel.getWeatherRus()
            binding.floatActionButton.setImageResource(R.drawable.russia_ic)
        }
        isDataSetRus = !isDataSetRus
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }
    private fun renderData(data: AppState){
        when(data){
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Toast.makeText(requireContext(),"error!", Toast.LENGTH_LONG).show()
            }
            is AppState.Loading -> binding.loadingLayout.visibility = View.VISIBLE
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(data.weatherData)
                Snackbar.make(binding.root, "Работает", Snackbar.LENGTH_LONG).show()

            }
        }
    }

}
