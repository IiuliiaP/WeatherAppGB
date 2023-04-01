package com.example.weatherappgb.view.weatherlist


import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappgb.R
import com.example.weatherappgb.databinding.FragmentWeatherListBinding
import com.example.weatherappgb.model.City
import com.example.weatherappgb.model.Weather
import com.example.weatherappgb.view.weatherdetails.WeatherDetailsFragment
import com.example.weatherappgb.viewmodel.AppState
import com.example.weatherappgb.viewmodel.WeatherListViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.util.*


private const val REQUEST_CODE_LOCATION = 44

class WeatherListFragment : Fragment() {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get(){
            return _binding!!
        }

    private val adapter = WeatherListFragmentAdapter(object: OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            openDetailsFragment(weather)
        }
        })
    private fun openDetailsFragment(weather: Weather){
        activity?.supportFragmentManager?.apply {
            beginTransaction().replace(R.id.fragment_container,WeatherDetailsFragment.newInstance(
                Bundle().apply {
                    putParcelable(WeatherDetailsFragment.KEY_BUNDLE_WEATHER, weather)
                })
            ).addToBackStack("").commitAllowingStateLoss()
        }
    }
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
        binding.floatActionButtonChangeSetup.setOnClickListener {changeWeatherDataSet()}
        binding.floatActionButtonLocation.setOnClickListener { checkPermission() }
        viewModel.getData().observe(viewLifecycleOwner, Observer {
            renderData(it) })
        viewModel.getWeatherRus()
    }
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            explain()
        } else {
            mRequestPermission()
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLocation(){
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                val providerGPS = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                providerGPS?.let {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        100f,
                        locationListenerDistance
                    )
                }
            }
        }
    }
    private val locationListenerDistance = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("@@@",location.toString())
            getAddressByLocation(location)
        }
        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }
        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

    }
    fun getAddressByLocation(location: Location){
        val geocoder = Geocoder(context!!)
        Thread{
            try {
                val addressText =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1,)!![0]
                        .getAddressLine(0)
                requireActivity().runOnUiThread {
                    showAddressDialog(addressText, location)
                }

            }catch (e: IOException){
                e.printStackTrace()
            }
        }.start()

    }
    private fun explain() {
        AlertDialog.Builder(requireContext())
            .setTitle(resources.getString(R.string.dialog_rationale_title))
            .setMessage(resources.getString(R.string.dialog_rationale_message))
            .setPositiveButton(resources.getString(R.string.dialog_rationale_give_access)) { _, _ ->
                mRequestPermission()
            }
            .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
    private fun mRequestPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CODE_LOCATION){
            for (i in permissions.indices) {
                if (permissions[i] == android.Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    explain()
                }
            }

        }else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                   openDetailsFragment(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherWorld()
            binding.floatActionButtonChangeSetup.setImageResource(R.drawable.ic_planet)
        } else {
            viewModel.getWeatherRus()
            binding.floatActionButtonChangeSetup.setImageResource(R.drawable.ic_russia)
        }
        isDataSetRus = !isDataSetRus
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onDestroy() {
        super.onDestroy()
        adapter.removeListener()
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
                adapter.setWeather(data.weatherList)
                Snackbar.make(binding.root, "Работает", Snackbar.LENGTH_LONG).show()

            }
        }
    }

}
