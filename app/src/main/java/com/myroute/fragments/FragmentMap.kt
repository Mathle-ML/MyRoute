package com.myroute.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myroute.MainActivity
import com.myroute.R
import com.myroute.dbmanager.DBManager
import com.myroute.models.Ruta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Suppress("DEPRECATION")
class FragmentMap : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MainActivity.toolbar.visibility = View.VISIBLE
        MainActivity.bottomBar.visibility = View.VISIBLE
        return inflater.inflate(R.layout.fragment_map, container, false).also {
            initView(it)
        }
    }

    private fun initView(view: View) {
        val mapView = view.findViewById<MapView>(R.id.mapView)
        setupMapView(mapView)
        if (hasLocationPermission) {
            getCurrentLocation()?.let { location ->
                addUserLocationMarker(mapView, location)
                mapView.controller.apply {
                    setCenter(location)
                    setZoom(19.0)
                }
            }
        }
        generateRouteIfExist(mapView)
    }

    private fun setupMapView(mapView: MapView) {
        mapView.apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            setBuiltInZoomControls(false)
            controller.setZoom(4.0)
        }
    }

    private fun getCurrentLocation(): GeoPoint? {
        return try {
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            location?.let {
                GeoPoint(it.latitude, it.longitude)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            null
        }
    }

    private fun generateRouteIfExist(mapView: MapView) {
        coroutineManager?.cancel()
        coroutineManager = CoroutineScope(Dispatchers.Main).launch {
            val dbManager = DBManager(MainActivity.mainContext)
            val route: Ruta = dbManager.getRoute(routToGenerate ?: return@launch) ?: return@launch
            when (route.getRouteType()) {
                "bus" -> generateBusRoute(route, mapView)
                "train" -> generateTrainRoute(route, mapView)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private suspend fun generateBusRoute(route: Ruta, mapView: MapView) {
        val road = withContext(Dispatchers.IO) {
            val roadManager = OSRMRoadManager(MainActivity.mainContext, OSRMRoadManager.MEAN_BY_CAR)
            roadManager.getRoad(route.getRefPoints())
        }
        val roadOverlay = RoadManager.buildRoadOverlay(road, route.getColor(), 15F)
        mapView.overlays.add(roadOverlay)

        val nodeIcon = resources.getDrawable(R.drawable.bus_stop)
        for (i in route.getRefStops().indices) {
            val nodeMarker = Marker(mapView)
            nodeMarker.apply {
                position = route.getRefStops()[i]
                icon = nodeIcon
            }
            mapView.overlays.add(nodeMarker)
        }

        mapView.invalidate()

        mapView.controller.apply {
            setCenter(road.mBoundingBox.centerWithDateLine)
            setZoom(14.0)
        }
    }

    private fun generateTrainRoute(route: Ruta, mapView: MapView) {
        val polyline = Polyline().apply {
            route.getRefPoints().forEach { addPoint(it) }
            color = route.getColor() // Cambia "Color.RED" por el color deseado
        }

        mapView.overlayManager.add(polyline)

        for (i in route.getRefStops()) {
            val marker = Marker(mapView)
            marker.apply {
                position = i
                icon = resources.getDrawable(R.drawable.train_stop)
            }
            mapView.overlayManager.add(marker)
        }

        mapView.controller.apply {
            setCenter(route.getRefPoints()[route.getRefPoints().size/2])
            setZoom(14.0)
        }

    }

    private fun addUserLocationMarker(mapView: MapView, location: GeoPoint) {
        val userMarker = Marker(mapView)
        userMarker.apply {
            position = location
            icon = resources.getDrawable(R.drawable.user_location) // Reemplaza "R.drawable.user_marker" con tu propio recurso de icono
            title = "User Location"
        }
        mapView.overlays.add(userMarker)
    }

    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"

        var hasLocationPermission = false
        var routToGenerate: String? = null
        private var coroutineManager: Job? = null

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMap().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}