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
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.setBuiltInZoomControls(false)
        mapView.controller.setZoom(4.0)

        if (hasLocationPermission) {
            val userLocation = getCurrentLocation()
            Log.i("MyRoute:Info", "Hola: ${userLocation.toString()}")
            userLocation?.let { location ->
                val userLocationMarker = Marker(mapView)
                userLocationMarker.position = location
                userLocationMarker.icon = MainActivity.mainContext.resources.getDrawable(R.drawable.user_location)
                userLocationMarker.title = "Mi ubicación"
                mapView.overlays.add(userLocationMarker)

                mapView.controller.setCenter(location)
                mapView.controller.setZoom(19.0)
            }
        }

        generateRouteIfExist(mapView)
    }

    private fun getCurrentLocation() : GeoPoint?{
        return try {
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            location?.let {
                val latitude = it.latitude
                val longitude = it.longitude
                GeoPoint(latitude, longitude)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            null
        }
    }

    fun generateRouteIfExist(mapView: MapView){
        coroutineManager?.cancel()
        coroutineManager = CoroutineScope(Dispatchers.Main).launch {
            val dbManager = DBManager(MainActivity.mainContext)
            val route: Ruta = dbManager.getRoute(routToGenerate ?: return@launch) ?: return@launch
            when(route.getRouteType()){
                "bus"->generateBusRoute(route, mapView)
                "train"->generateTrainRoute(route, mapView)
            }


        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    suspend fun generateBusRoute(route: Ruta, mapView: MapView) {

            val road = withContext(Dispatchers.IO) {
                val roadManager = OSRMRoadManager(MainActivity.mainContext, OSRMRoadManager.MEAN_BY_CAR)
                roadManager.getRoad(route.getRefPoints())
            }
            val roadOverlay = RoadManager.buildRoadOverlay(road, route.getColor(), 15F)
            mapView.overlays.add(roadOverlay)

            val nodeIcon = MainActivity.mainContext.resources.getDrawable(R.drawable.bus_stop)
            for (i in route.getRefStops().indices) {
                val nodeMarker = Marker(mapView)
                nodeMarker.position = route.getRefStops()[i]
                nodeMarker.icon = nodeIcon
                nodeMarker.title = "Step $i"
                mapView.overlays.add(nodeMarker)
            }

            mapView.invalidate()

            mapView.controller.setCenter(road.mBoundingBox.centerWithDateLine)
            mapView.controller.setZoom(14.0)

    }

    fun generateTrainRoute(route: Ruta, mapView: MapView){
        val polyline = Polyline()

        for (point in route.getRefPoints()) {
            polyline.addPoint(point)
        }

        polyline.color = Color.RED // Cambia "Color.RED" por el color deseado

        for (i in route.getRefStops()) {
            val marker = Marker(mapView)
            marker.position = i
            marker.icon = resources.getDrawable(R.drawable.bus_stop) // Reemplaza "R.drawable.marker_icon" con tu propio recurso de icono
            mapView.overlayManager.add(marker)
        }

        mapView.overlayManager.add(polyline)
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







