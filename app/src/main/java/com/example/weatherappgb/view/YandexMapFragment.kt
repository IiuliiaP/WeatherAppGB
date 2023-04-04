package com.example.weatherappgb.view


import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weatherappgb.R
import com.example.weatherappgb.YANDEX_KEY_MAPKIT
import com.example.weatherappgb.databinding.FragmentYandexMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError


class YandexMapFragment : Fragment(), UserLocationObjectListener, Session.SearchListener, CameraListener{
    lateinit var mapView: MapView
    lateinit var locationMap: UserLocationLayer
    private var _binding: FragmentYandexMapBinding? = null
    private val binding: FragmentYandexMapBinding
        get() {
            return _binding!!
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(YANDEX_KEY_MAPKIT )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MapKitFactory.initialize(requireContext())
        mapView = binding.mapview
        mapView.map.move(
            CameraPosition( Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),null)
        checkPermission()
        SearchFactory.initialize(requireContext())
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        mapView.map.addCameraListener(this)
      binding.searchAddress.setOnEditorActionListener { v, action, keyEvent ->
          if(action == EditorInfo.IME_ACTION_SEARCH){
              initSearchByAddress(binding.searchAddress.text.toString())
          }
          false
      }

    }
    lateinit var searchManager: SearchManager
    lateinit var searchSession: Session
    private fun initSearchByAddress(query: String){
        searchSession = searchManager.submit(
           query,VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
           SearchOptions(),this
       )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYandexMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
     private fun getLocation(){
         val mapKit: MapKit = MapKitFactory.getInstance()
         locationMap= mapKit.createUserLocationLayer(mapView.mapWindow)
         locationMap.isVisible = true
         locationMap.setObjectListener(this)
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
        requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 0){
            for (i in permissions.indices) {
                if (permissions[i] == android.Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    explain()
                }
            }

        }else super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onStart() {
        super.onStart()
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
    companion object {
        @JvmStatic
        fun newInstance() = YandexMapFragment()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        locationMap.setAnchor(
            PointF((mapView.width() * 0.5).toFloat(), (mapView.height() * 0.5).toFloat()),
            PointF((mapView.width() * 0.85).toFloat(), (mapView.height() * 0.85).toFloat())
        )

    }

    override fun onObjectRemoved(p0: UserLocationView) {
        TODO("Not yet implemented")
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
        TODO("Not yet implemented")
    }

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        finished: Boolean
    ) {
        if(finished){
            initSearchByAddress(binding.searchAddress.text.toString())
        }
    }

    override fun onSearchResponse(response: Response) {
        val mapObject: MapObjectCollection = mapView.map.mapObjects
        mapObject.clear()
        for(searchResult in response.collection.children) {
            val resultLocation = searchResult.obj!!.geometry[0].point!!
            if(response!= null){
                mapObject.addPlacemark(resultLocation,ImageProvider.fromResource(requireContext(),R.drawable.ic_navigation))
            }
        }
    }

    override fun onSearchError(e: Error) {
        var errorMessage = "Неизвестная ошибка"
        if(e is RemoteError){
            errorMessage = "Проблема с загрузкой"
        }else if(e is NetworkError){
            errorMessage = "Проблема с интернетом"
        }
        Toast.makeText(requireContext(),errorMessage, Toast.LENGTH_LONG).show()

    }
}